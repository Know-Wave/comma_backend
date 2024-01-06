package know_wave.comma.arduino.order.service;

import jakarta.transaction.Transactional;
import know_wave.comma.account.entity.Account;
import know_wave.comma.arduino.cart.entity.Cart;
import know_wave.comma.arduino.cart.repository.CartRepository;
import know_wave.comma.arduino.notification.dto.OrderNotificationRequest;
import know_wave.comma.arduino.notification.service.ArduinoOrderNotification;
import know_wave.comma.arduino.order.dto.OrderCallbackResponse;
import know_wave.comma.arduino.order.entity.Order;
import know_wave.comma.arduino.order.entity.OrderDetail;
import know_wave.comma.arduino.order.entity.OrderStatus;
import know_wave.comma.arduino.order.exception.OrderException;
import know_wave.comma.arduino.order.repository.OrderDetailRepository;
import know_wave.comma.arduino.order.repository.OrderRepository;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.payment.dto.gateway.*;
import know_wave.comma.payment.entity.Payment;
import know_wave.comma.payment.entity.PaymentFeature;
import know_wave.comma.payment.service.PaymentCallbackHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/* TODO
     * 1. 주문 성공 시 주문 상품들 장바구니에서 지우기
 */
@Service
@Transactional
@RequiredArgsConstructor
public class DepositPaymentCallbackHandler implements PaymentCallbackHandler {

    // 가상 번호
    private static final int KAKAOPAY_FAILURE_CODE = -1039930293;
    private final ArduinoOrderNotification orderNotification;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartRepository cartRepository;

    private static final PaymentFeature ARDUINO_DEPOSIT = PaymentFeature.ARDUINO_DEPOSIT;
    private static final String ORDER_STATUS = "orderStatus";
    private static final String DEPOSIT_STATUS = "depositStatus";

    @Value("${arduino.max-order-quantity}")
    private int ORDER_MAX_QUANTITY;

    @Override
    public CompleteCallbackResponse complete(CompleteCallback completeCallback) {
        String orderNumber = completeCallback.getOrderNumber();

        OrderCallbackResponse orderResponse = order(orderNumber);

        OrderNotificationRequest orderNotificationRequest =
                OrderNotificationRequest.create(orderResponse.getOrderStatus(), orderResponse.getDepositStatus(),
                        orderNumber, completeCallback.getAccountId());

        orderNotification.notify(orderNotificationRequest);

        Map<String, String> response = Map.of(ORDER_STATUS, orderResponse.getOrderStatus().getStatus(),
                DEPOSIT_STATUS, orderResponse.getDepositStatus().getStatus());

        return CompleteCallbackResponse.create(response);
    }

    @Override
    public CancelCallbackResponse cancel(CancelCallback cancelCallback) {
        OrderCallbackResponse orderResponse = failOrder(cancelCallback.getOrderNumber(),
                OrderStatus.FAILURE_CAUSE_DEPOSIT_CANCEL);

        Map<String, String> response = Map.of(ORDER_STATUS, orderResponse.getOrderStatus().getStatus(),
                DEPOSIT_STATUS, orderResponse.getDepositStatus().getStatus());

        return CancelCallbackResponse.create(response);
    }

    @Override
    public FailCallbackResponse fail(FailCallback failCallback) {
        OrderCallbackResponse orderResponse = failOrder(failCallback.getOrderNumber(),
                OrderStatus.FAILURE_CAUSE_DEPOSIT_FAILURE);

        Map<String, String> response = Map.of(ORDER_STATUS, orderResponse.getOrderStatus().getStatus(),
                DEPOSIT_STATUS, orderResponse.getDepositStatus().getStatus());

        return FailCallbackResponse.create(response);
    }

    @Override
    public void error(ErrorCallback errorCallback) {
        int errorCode = errorCallback.getErrorCode();

        OrderCallbackResponse orderCallbackResponse;
        OrderNotificationRequest orderNotificationRequest;

        if (errorCode == KAKAOPAY_FAILURE_CODE) {
            orderCallbackResponse = failOrder(errorCallback.getOrderNumber(), OrderStatus.FAILURE_CAUSE_DEPOSIT_FAILURE);

            orderNotificationRequest = OrderNotificationRequest.create(orderCallbackResponse.getOrderStatus(), orderCallbackResponse.getDepositStatus(),
                            errorCallback.getOrderNumber(), errorCallback.getAccountId());

            orderNotification.notify(orderNotificationRequest);
            return;
        }

        orderCallbackResponse = failOrder(errorCallback.getOrderNumber(), OrderStatus.FAILURE_CAUSE_SERVER);

        orderNotificationRequest = OrderNotificationRequest.create(orderCallbackResponse.getOrderStatus(), orderCallbackResponse.getDepositStatus(),
                        errorCallback.getOrderNumber(), errorCallback.getAccountId());

        orderNotification.notify(orderNotificationRequest);
    }

    @Override
    public boolean isSupport(PaymentFeature paymentFeature) {
        return ARDUINO_DEPOSIT == paymentFeature;
    }

    /* 사용자 결제 성공 시 호출됨
     * 1. 보증금 결제 상태 확인
     * 2. 아두이노 부품 상태 및 재고 확인
     * 2.1 재고 부족 시 예외 처리 (보증금 환불, 주문 취소 처리)
     * 3. 주문 접수(주문 및 보증금 상태 변경)
     * 4. 재고 차감
     * 5. 주문 및 보증금 상태 반환
     */

    private OrderCallbackResponse order(String orderNumber) {
        Order order = findOrderByOrderNumber(orderNumber);
        List<OrderDetail> orderDetails = findOrderDetails(order);
        Payment payment = order.getDeposit().getPayment();

        if (!payment.isPaymentComplete()) {
            throw new OrderException(ExceptionMessageSource.UNABLE_TO_ORDER);
        }

        OrderStatus orderStatus = Order.validateList(orderDetails, ORDER_MAX_QUANTITY);

        if (orderStatus.isFailureCauseStockStatus()) {
            order.handleFailureDuringOrderProcessing(OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK_STATUS);
            return OrderCallbackResponse.create(order.getOrderStatus(), order.getDeposit().getDepositStatus());
        } else if (orderStatus.isFailureCauseStock()) {
            order.handleFailureDuringOrderProcessing(OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK);
            return OrderCallbackResponse.create(order.getOrderStatus(), order.getDeposit().getDepositStatus());
        }

        order.order(orderDetails);
        removeOrderedComponent(order, orderDetails);

        return OrderCallbackResponse.create(order.getOrderStatus(), order.getDeposit().getDepositStatus());
    }
    private void removeOrderedComponent(Order order, List<OrderDetail> orderDetails) {
        Account account = order.getAccount();
        List<Cart> carts = cartRepository.findAllByAccount(account);

        carts.forEach(cart -> {
            orderDetails.forEach(orderDetail -> {
                if (cart.getArduino().getId().equals(orderDetail.getArduino().getId())) {
                    cartRepository.delete(cart);
                }
            });
        });
    }

    private OrderCallbackResponse failOrder(String orderNumber, OrderStatus orderStatus) {
        Order order = findOrderByOrderNumber(orderNumber);

        order.handleFailureCauseDepositPayment(orderStatus);

        return OrderCallbackResponse.create(order.getOrderStatus(), order.getDeposit().getDepositStatus());
    }

    private Order findOrderByOrderNumber(String orderNumber) {
        return orderRepository.findFetchByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderException(ExceptionMessageSource.INVALID_ORDER_NUMBER));
    }

    private List<OrderDetail> findOrderDetails(Order order) {
        return orderDetailRepository.findFetchAllByOrder(order);
    }
}

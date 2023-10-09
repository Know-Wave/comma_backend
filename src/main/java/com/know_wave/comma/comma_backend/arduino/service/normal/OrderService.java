package com.know_wave.comma.comma_backend.arduino.service.normal;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.service.normal.AccountQueryService;
import com.know_wave.comma.comma_backend.arduino.dto.basket.BasketDeleteRequest;
import com.know_wave.comma.comma_backend.arduino.dto.basket.BasketRequest;
import com.know_wave.comma.comma_backend.arduino.dto.basket.BasketResponse;
import com.know_wave.comma.comma_backend.arduino.dto.order.OrderCancelRequest;
import com.know_wave.comma.comma_backend.arduino.dto.order.OrderDetailResponse;
import com.know_wave.comma.comma_backend.arduino.dto.order.OrderRequest;
import com.know_wave.comma.comma_backend.arduino.dto.order.OrderResponse;
import com.know_wave.comma.comma_backend.arduino.entity.*;
import com.know_wave.comma.comma_backend.arduino.repository.BasketRepository;
import com.know_wave.comma.comma_backend.arduino.repository.OrderInfoRepository;
import com.know_wave.comma.comma_backend.exception.EntityAlreadyExistException;
import com.know_wave.comma.comma_backend.util.GenerateCodeUtils;
import com.know_wave.comma.comma_backend.util.ValidateUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.know_wave.comma.comma_backend.account.service.normal.AccountQueryService.getAuthenticatedId;
import static com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource.*;

@Service
@Transactional
public class OrderService {

    private final AccountQueryService accountQueryService;
    private final BasketService basketService;
    private final BasketRepository basketRepository;
    private final OrderInfoRepository orderInfoRepository;

    public OrderService(AccountQueryService accountQueryService, BasketService basketService, BasketRepository basketRepository, OrderInfoRepository orderInfoRepository) {
        this.accountQueryService = accountQueryService;
        this.basketService = basketService;
        this.basketRepository = basketRepository;
        this.orderInfoRepository = orderInfoRepository;
    }

    public void order(OrderRequest request) {

        Account account = accountQueryService.findAccount(getAuthenticatedId());
        List<Basket> baskets = basketService.getBasketsFetchArduino(account);

        if (Basket.isOverRequest(baskets) || Basket.isEmpty(baskets)) {
            throw new IllegalArgumentException(NOT_ACCEPTABLE_REQUEST);
        }

        String orderNumber = GenerateCodeUtils.getCodeWithDate();
        OrderInfo orderInfo = new OrderInfo(account, orderNumber, request.getSubject(), request.getDescription());

        List<Order> orders = Order.ofList(baskets, orderInfo);

        orderInfoRepository.save(orderInfo);
        basketRepository.deleteAll(baskets);
    }

    public List<OrderResponse> getOrders() {

        final String accountId = getAuthenticatedId();
        Account account = accountQueryService.findAccount(accountId);
        List<OrderInfo> orderInfos = getOrderInfos(account);

        return OrderResponse.ofList(orderInfos);
    }

    public OrderDetailResponse getOrderDetail(String orderNumber) {

        final String accountId = getAuthenticatedId();
        Account account = accountQueryService.findAccount(accountId);
        OrderInfo orderInfo = getOrderInfo(orderNumber);

        if (orderInfo.isNotOrderer(account)) {
            throw new BadCredentialsException(BAD_CREDENTIALS);
        }

        var orderArduinoList = new ArrayList<OrderDetailResponse.Arduino>();

        OrderDetailResponse result = new OrderDetailResponse(
                accountId,
                orderInfo.getDescription(),
                orderInfo.getCreatedDate(),
                orderInfo.getStatus().getValue(),
                orderInfo.getOrderNumber(),
                orderInfo.getSubject(),
                orderInfo.getCancellationReason(),
                orderArduinoList
        );

        orderInfo.getOrders().forEach(order ->
                orderArduinoList.add(
                        new OrderDetailResponse.Arduino(order.getArduino().getId(),
                                order.getArduino().getName(),
                                order.getCount(),
                                order.getArduino().getCategories())
                )
        );

        return result;
    }

    public void cancelOrderRequest(String orderNumber, OrderCancelRequest request) {

        Account account = accountQueryService.findAccount(getAuthenticatedId());

        orderInfoRepository.findById(orderNumber).ifPresentOrElse(orderInfo -> {

                if (orderInfo.isNotOrderer(account)) {
                    throw new BadCredentialsException(BAD_CREDENTIALS);
                }

                if (!orderInfo.isCancellable()) {
                    String message = NOT_ACCEPTABLE_ORDER_STATUS(orderInfo.getStatus());
                    throw new IllegalArgumentException(message);
                }

                orderInfo.setStatus(OrderStatus.CANCELLATION_REQUEST);
                orderInfo.setCancellationReason(request.getReason());
            },
            () -> {
                throw new EntityNotFoundException(NOT_FOUND_VALUE);
            }
        );
    }

    private OrderInfo getOrderInfo(String orderNumber) {
        return orderInfoRepository.findFetchById(orderNumber)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_VALUE));
    }

    private List<OrderInfo> getOrderInfos(Account account) {
        List<OrderInfo> orderInfos = orderInfoRepository.findAllByAccount(account);

        ValidateUtils.throwIfEmpty(orderInfos);
        return orderInfos;
    }

}

package know_wave.comma.payment_.service;

import know_wave.comma.alarm.util.ExceptionMessageSource;
import know_wave.comma.order_.entity.OrderInfo;
import know_wave.comma.order_.service.user.OrderInfoQueryService;
import know_wave.comma.payment_.entity.Deposit;
import know_wave.comma.payment_.entity.DepositStatus;
import know_wave.comma.payment_.entity.PaymentType;
import know_wave.comma.payment_.exception.AlreadyPaidException;
import know_wave.comma.payment_.exception.AlreadyRefundedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentManager {

    private final List<PaymentService> paymentServices;
    private final OrderInfoQueryService orderInfoQueryService;
    private final DepositQueryService depositQueryService;

    public PaymentService getPaymentService(PaymentType paymentType) throws IllegalArgumentException {
        return paymentServices.stream()
                .filter(paymentService -> paymentService.supports(paymentType))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(ExceptionMessageSource.NOT_SUPPORTED_PAYMENT_TYPE));
    }

    public void checkAlreadyPaid(String orderNumber) throws AlreadyPaidException {
        Optional<OrderInfo> orderInfoOptional = orderInfoQueryService.optionalFetchAccount(orderNumber);

        if (orderInfoOptional.isPresent()) {
            OrderInfo orderInfo = orderInfoOptional.get();
            List<Deposit> deposits = depositQueryService.getDeposits(orderInfo.getAccount(), orderInfo);

            if (deposits.isEmpty()) {
                return;
            }

            boolean paid = deposits.stream().anyMatch(deposit -> deposit.getDepositStatus().equals(DepositStatus.PAID));

            if (paid) {
                throw new AlreadyPaidException(ExceptionMessageSource.ALREADY_PAID);
            }
        }
    }

    public void checkAlreadyRefund(Deposit deposit) throws AlreadyRefundedException {
        if (deposit.getDepositStatus().equals(DepositStatus.REFUND)) {
            throw new AlreadyRefundedException(ExceptionMessageSource.ALREADY_REFUNDED);
        }
    }
}

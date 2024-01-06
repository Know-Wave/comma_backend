package know_wave.comma.payment.dto.gateway;

import know_wave.comma.payment.entity.PaymentFeature;
import know_wave.comma.payment.entity.PaymentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PaymentGatewayCancelRequest {

    public static PaymentGatewayCancelRequest create(String paymentRequestId, String orderNumber, String accountId, PaymentType paymentType, PaymentFeature paymentFeature) {
        return new PaymentGatewayCancelRequest(paymentRequestId, orderNumber, accountId, paymentType, paymentFeature);
    }

    private final String paymentRequestId;
    private final String orderNumber;
    private final String accountId;
    private final PaymentType paymentType;
    private final PaymentFeature paymentFeature;

}

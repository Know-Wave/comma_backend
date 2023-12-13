package know_wave.comma.payment.dto.gateway;

import know_wave.comma.payment.entity.PaymentFeature;
import know_wave.comma.payment.entity.PaymentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PaymentGatewayApproveRequest {

    public static PaymentGatewayApproveRequest of(String paymentRequestId, String orderNumber, String accountId, String paymentType, String paymentFeature, String pgToken) {
        return new PaymentGatewayApproveRequest(paymentRequestId, orderNumber, accountId, paymentType, paymentFeature, pgToken);
    }

    private final String paymentRequestId;
    private final String orderNumber;
    private final String accountId;
    private final String paymentType;
    private final String paymentFeature;
    private final String pgToken;
}

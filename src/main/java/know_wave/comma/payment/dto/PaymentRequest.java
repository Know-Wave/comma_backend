package know_wave.comma.payment.dto;

import know_wave.comma.payment.entity.PaymentType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentRequest {

    @NotEmpty(message = "{Required}")
    String accountId;
    @NotEmpty(message = "{Required}")
    String arduinoOrderId;
    @NotNull(message = "{Required}")
    PaymentType paymentType;
}
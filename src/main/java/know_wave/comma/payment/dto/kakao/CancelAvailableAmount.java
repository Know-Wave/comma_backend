package know_wave.comma.payment.dto.kakao;

public record CancelAvailableAmount(
        int total,
        int tax_free,
        int vat,
        int point,
        int discount,
        int green_deposit
) {
}
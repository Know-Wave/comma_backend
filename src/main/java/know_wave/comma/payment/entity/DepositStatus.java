package know_wave.comma.payment.entity;

public enum DepositStatus {

    NONE("보증금 제출 안함"),
    PAID("보증금 제출"),
    REFUND("보증금 반환"),
    DONATE("보증금 기부");

    private final String value;

    DepositStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
package know_wave.comma.common.notification.push.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NotificationFeature {

    ACCOUNT_VERIFY_EMAIL("accountVerifyEmail"),
    ACCOUNT("account"),
    ARDUINO_ORDER("arduinoOrder"),
    ARDUINO_RESTOCK("arduinoRestock"),
    ARDUINO_COMMENT("arduinoComment"),
    COMMUNITY("community");

    final String feature;
}

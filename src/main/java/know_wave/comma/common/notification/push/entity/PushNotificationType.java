package know_wave.comma.common.notification.push.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PushNotificationType {

    WEB("web"),
    EMAIL("email"),
    KAKAOTALK("kakaotalk");

    private final String type;
}

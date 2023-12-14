package know_wave.comma.notification.push.service;

import know_wave.comma.notification.push.entity.PushNotificationType;

import java.util.Map;

interface PushNotificationSender {

    void send(String dest, String title, String content, Map<Object, Object> paramMap);

    boolean isSupport(PushNotificationType type);
}

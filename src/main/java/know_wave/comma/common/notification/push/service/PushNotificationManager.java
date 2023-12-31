package know_wave.comma.common.notification.push.service;

import know_wave.comma.common.notification.push.dto.AccountEmailNotificationRequest;
import know_wave.comma.common.notification.push.dto.PushNotificationRequest;
import know_wave.comma.common.notification.push.entity.PushNotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PushNotificationManager {

    private final List<PushNotificationSender> notificationSenders;

    public void send(PushNotificationRequest notificationRequest, Set<PushNotificationType> allowedTypes) {
        Map<PushNotificationType, String> destMap = notificationRequest.getDestMap();

        destMap.forEach((type, dest) ->
            notificationSenders
                .forEach(sender -> {
                    if (sender.isSupport(type) && allowedTypes.contains(type)) {
                        sender.send(dest, notificationRequest.getTitle(), notificationRequest.getContent(), notificationRequest.getDataMap());
                    }
                }));
    }

    public void sendVerifyEmail(AccountEmailNotificationRequest notificationRequest) {
        notificationSenders.stream()
            .filter(sender -> sender.isSupport(PushNotificationType.EMAIL))
            .findFirst()
            .ifPresent(sender ->
                sender.send(notificationRequest.getDest(), notificationRequest.getTitle(), notificationRequest.getContent(), null));
    }

}

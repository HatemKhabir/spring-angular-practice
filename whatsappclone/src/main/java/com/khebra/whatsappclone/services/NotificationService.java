package com.khebra.whatsappclone.services;


import com.khebra.whatsappclone.models.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
private final SimpMessagingTemplate messagingTemplate;

public void sendNotification(String userId, Notification notification){
    log.info("sending socket notification to {} with payload {}",userId,notification);
    messagingTemplate.convertAndSendToUser(userId,"/chat",notification);
}
}

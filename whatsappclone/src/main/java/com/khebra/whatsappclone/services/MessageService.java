package com.khebra.whatsappclone.services;


import com.khebra.whatsappclone.common.MessageState;
import com.khebra.whatsappclone.common.MessageType;
import com.khebra.whatsappclone.common.NotificationType;
import com.khebra.whatsappclone.mappers.FileUtils;
import com.khebra.whatsappclone.mappers.MessageMapper;
import com.khebra.whatsappclone.models.Chat;
import com.khebra.whatsappclone.models.Message;
import com.khebra.whatsappclone.models.Notification;
import com.khebra.whatsappclone.repositories.ChatRepository;
import com.khebra.whatsappclone.repositories.MessageRepository;
import com.khebra.whatsappclone.requests.MessageRequest;
import com.khebra.whatsappclone.response.MessageResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;
    private final FileService fileService;
    private final NotificationService notificationService;
    public void saveMessage(MessageRequest messageRequest){
        Chat chat=chatRepository.findById(messageRequest.getChatId()).orElseThrow(()->new EntityNotFoundException("Chat Not Found !"));
        Message message=new Message();
        message.setSenderId(messageRequest.getSenderId());
        message.setReceiverId(messageRequest.getReceiverId());
        message.setType(messageRequest.getType());
        message.setChat(chat);
        message.setState(MessageState.SENT);

        messageRepository.save(message);
        Notification notification=Notification.builder()
                .chatId(chat.getId())
                .messageType(messageRequest.getType())
                .content(messageRequest.getContent())
                .senderId(messageRequest.getSenderId())
                .receiverId(messageRequest.getReceiverId())
                .type(NotificationType.MESSAGE)
                .chatName(chat.getChatName(message.getSenderId()))
                .build();
        notificationService.sendNotification(message.getReceiverId(),notification);
    }


    @Transactional
    public List<MessageResponse> findChatMessages(String chatId){
    return messageRepository.findMessagesByChatId(chatId).stream().map(messageMapper::toMessageResponse).toList();
    }

    public void updateMessageState(String chatId, Authentication authentication){
        Chat chat=chatRepository.findById(chatId)
                .orElseThrow(()->new EntityNotFoundException("Chat not Found"));
        final String receiverId=getRecipientId(chat,authentication);
        messageRepository.updateMessageState(chat.getId(),MessageState.SEEN);

        Notification notification=Notification.builder()
                .chatId(chat.getId())
                .senderId(getSenderId(chat,authentication))
                .receiverId(receiverId)
                .type(NotificationType.SEEN)
                .build();
        notificationService.sendNotification(receiverId,notification);

    }

    private String getRecipientId(Chat chat, Authentication authentication) {
        if (chat.getSender().getId().equals(authentication.getName())){
            return chat.getReceiver().getId();
        }
        return chat.getSender().getId();
    }

    private String getSenderId(Chat chat,Authentication authentication){
        if (chat.getSender().getId().equals(authentication.getName())){
            return chat.getSender().getId();
        }
        return chat.getReceiver().getId();
    }

    public void uploadMediaMessage(String chatId, MultipartFile file, Authentication authentication) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat not found"));
        final String senderId = getSenderId(chat, authentication);
        final String receiverId = getRecipientId(chat, authentication);
        final String filePath = fileService.saveFile(file, senderId);
        Message message = new Message();
        message.setChat(chat);
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setType(MessageType.IMAGE);
        message.setState(MessageState.SENT);
        message.setMediaFilePath(filePath);
        messageRepository.save(message);
        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .type(NotificationType.IMAGE)
                .messageType(MessageType.IMAGE)
                .senderId(senderId)
                .receiverId(receiverId)
                .media(FileUtils.readFileFromLocation(filePath))
                .build();
        notificationService.sendNotification(message.getReceiverId(), notification);
    }

    }

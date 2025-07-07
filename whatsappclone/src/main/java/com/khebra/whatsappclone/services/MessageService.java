package com.khebra.whatsappclone.services;


import com.khebra.whatsappclone.common.MessageState;
import com.khebra.whatsappclone.mappers.MessageMapper;
import com.khebra.whatsappclone.models.Chat;
import com.khebra.whatsappclone.models.Message;
import com.khebra.whatsappclone.repositories.ChatRepository;
import com.khebra.whatsappclone.repositories.MessageRepository;
import com.khebra.whatsappclone.requests.MessageRequest;
import com.khebra.whatsappclone.response.MessageResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;


    public void saveMessage(MessageRequest messageRequest){
        Chat chat=chatRepository.findById(messageRequest.getChatId()).orElseThrow(()->new EntityNotFoundException("Chat Not Found !"));
        Message message=new Message();
        message.setSenderId(messageRequest.getSenderId());
        message.setReceiverId(messageRequest.getReceiverId());
        message.setType(messageRequest.getType());
        message.setChat(chat);
        message.setState(MessageState.SENT);

        messageRepository.save(message);
        //todo realtime notifcation
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
        //todo notif

    }

    private String getRecipientId(Chat chat, Authentication authentication) {
        if (chat.getSender().getId().equals(authentication.getName())){
            return chat.getReceiver().getId();
        }
        return chat.getSender().getId();
    }

}

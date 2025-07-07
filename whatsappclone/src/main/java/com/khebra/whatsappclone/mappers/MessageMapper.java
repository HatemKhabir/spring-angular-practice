package com.khebra.whatsappclone.mappers;


import com.khebra.whatsappclone.models.Message;
import com.khebra.whatsappclone.response.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public class MessageMapper {

    public MessageResponse toMessageResponse(Message message){
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .receiverId(message.getReceiverId())
                .senderId(message.getSenderId())
                .state(message.getState())
                .createdAt(message.getCreatedDate())
                .type(message.getType())
                .build();
    }

}

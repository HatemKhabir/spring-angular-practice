package com.khebra.whatsappclone.mappers;

import com.khebra.whatsappclone.models.Chat;
import com.khebra.whatsappclone.response.ChatResponse;
import org.springframework.stereotype.Service;

@Service
public class ChatMapper {
    public ChatResponse toChatResponse(Chat c, String userId) {
            return ChatResponse.builder()
                    .id(c.getId())
                    .name(c.getChatName(userId))
                    .unreadCount(c.getUnreadMessages(userId))
                    .lastMessage(c.getLastMessage())
                    .isRecipientOnline(c.getReceiver().isUserOnline())
                    .senderId(c.getSender().getId()).receiverId(c.getReceiver().getId())
                    .build();
    }
}

package com.khebra.whatsappclone.requests;

import com.khebra.whatsappclone.common.MessageType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequest {
private String content;
private String senderId;
private String receiverId;
private MessageType type;
private String chatId;

}

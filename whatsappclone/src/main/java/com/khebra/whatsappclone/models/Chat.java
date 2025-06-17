package com.khebra.whatsappclone.models;

import com.khebra.whatsappclone.common.BaseAuditingEntity;

import com.khebra.whatsappclone.common.BaseAuditingEntity;
import com.khebra.whatsappclone.common.MessageState;
import com.khebra.whatsappclone.common.MessageType;
import com.khebra.whatsappclone.constants.ChatConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="chats")
@NamedQuery(name = ChatConstants.FIND_CHAT_BY_SENDER_ID,
query = "SELECT DISTINCT c FROM Chat c WHERE c.sender.id= :senderId OR c.receiver.id= :senderId ORDER BY c.createdDate DESC")
@NamedQuery(name = ChatConstants.FIND_CHAT_BY_SENDER_ID_AND_RECEIVER,
query = "SELECT DISTINCT c FROM Chat  c WHERE (c.sender.id=: senderId AND c.receiver.id= :receiverId) OR (c.sender.id=:receiverId  AND c.receiver.id= :senderId)")
public class Chat extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @OneToMany(mappedBy = "chat",fetch = FetchType.EAGER)
    @OrderBy("createdDate DESC")
    private List<Message> messages;

    @Transient
    public String getChatName(String senderId){
        if (receiver.getId().equals(senderId)){
            return sender.getFirstName()+" "+sender.getLastName();
        }
        return receiver.getFirstName()+" "+receiver.getLastName();
    }

    @Transient
    public long getUnreadMessages(String senderId){
        return messages
                .stream()
                .filter(m -> m.getReceiverId().equals(senderId))
                .filter(m -> m.getState()== MessageState.SENT)
                .count();
    }

    @Transient
    public String getLastMessage(){
        if (messages!=null && !messages.isEmpty()){
            if (messages.getFirst().getType()!= MessageType.TEXT){
                return "Attachement";
            }
            return messages.getFirst().getContent();
        }
        return null;
    }

    @Transient
    public LocalDateTime getLastMessageTime(){
        if (messages!=null && !messages.isEmpty()){
           return messages.getFirst().getCreatedDate();
        }
        return null;
    }
}

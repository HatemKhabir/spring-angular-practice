package com.khebra.whatsappclone.repositories;

import com.khebra.whatsappclone.constants.ChatConstants;
import com.khebra.whatsappclone.mappers.ChatResponse;
import com.khebra.whatsappclone.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat,String> {
    @Query(name = ChatConstants.FIND_CHAT_BY_SENDER_ID)
    List<Chat> findBySenderId(@Param("senderId") String userId);

    @Query(name = ChatConstants.FIND_CHAT_BY_SENDER_ID_AND_RECEIVER)
    Optional<Chat> findByRecieverIdAndSenderId(@Param("senderId") String senderId,@Param("receiverId") String receiverId);
}

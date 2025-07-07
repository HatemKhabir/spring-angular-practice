package com.khebra.whatsappclone.repositories;

import com.khebra.whatsappclone.common.MessageState;
import com.khebra.whatsappclone.constants.MessageConstants;
import com.khebra.whatsappclone.models.Chat;
import com.khebra.whatsappclone.models.Message;
import com.khebra.whatsappclone.response.MessageResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(name= MessageConstants.FIND_MESSAGES_BY_CHAT_ID)
   //try to test with findByChatId
    List<Message> findMessagesByChatId(String chatId);

    @Query(name = MessageConstants.SET_MESSAGES_TO_SEEN_BY_CHAT)
    @Modifying
    void updateMessageState(String chatId, MessageState newState);
}

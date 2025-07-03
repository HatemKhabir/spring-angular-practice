package com.khebra.whatsappclone.repositories;

import com.khebra.whatsappclone.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}

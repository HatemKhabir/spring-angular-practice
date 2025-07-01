package com.khebra.whatsappclone.repositories;

import com.khebra.whatsappclone.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat,String> {

}

package com.khebra.whatsappclone.services;


import com.khebra.whatsappclone.mappers.ChatMapper;
import com.khebra.whatsappclone.mappers.ChatResponse;
import com.khebra.whatsappclone.models.Chat;
import com.khebra.whatsappclone.models.User;
import com.khebra.whatsappclone.repositories.ChatRepository;
import com.khebra.whatsappclone.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public List<ChatResponse> getChatsByReceiverId(Authentication currentUser){
        final String userId=currentUser.getName();
        return chatRepository.findBySenderId(userId)
                .stream()
                .map(c -> chatMapper.toChatResponse(c,userId)).toList();
    }

    public String createChat(String senderId,String receiverId){
        Optional<Chat> existingChat=chatRepository.findByRecieverIdAndSenderId(senderId,receiverId);
        if (existingChat.isPresent()){
            return existingChat.get().getId();
        }
        User sender=userRepository.findByPublicId(senderId)
                .orElseThrow(()->new EntityNotFoundException("User not Found"));
        User receiver=userRepository.findByPublicId(receiverId)
                .orElseThrow(()->new EntityNotFoundException("User not Found"));
    Chat chat=new Chat();
    chat.setSender(sender);
    chat.setReceiver(receiver);
    Chat savedChat=chatRepository.save(chat);
    return savedChat.getId();
    }

}

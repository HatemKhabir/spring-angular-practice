package com.khebra.whatsappclone.controllers;


import com.khebra.whatsappclone.mappers.ChatResponse;
import com.khebra.whatsappclone.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<String> createChat(
            @RequestParam(name = "sender_id") String senderId,
            @RequestParam(name = "receiver_id") String receiverId
    ){
        final String chatId= chatService.createChat(senderId,receiverId);
        return ResponseEntity.ok(chatId);
    }

    @GetMapping
    public ResponseEntity<List<ChatResponse>> getChatsByReceiver(Authentication currentUser){
        return ResponseEntity.ok(chatService.getChatsByReceiverId(currentUser));
    }
}

package com.khebra.whatsappclone.controllers;


import com.khebra.whatsappclone.response.ChatResponse;
import com.khebra.whatsappclone.services.ChatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
@Tag(name = "Chat")
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

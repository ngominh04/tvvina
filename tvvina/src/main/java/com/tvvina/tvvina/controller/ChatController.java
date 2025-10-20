package com.tvvina.tvvina.controller;

// ChatController.java
import com.tvvina.tvvina.domain.ChatMessage;
import com.tvvina.tvvina.respository.ChatMessageRepository;
import com.tvvina.tvvina.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class ChatController {
    @Autowired
    ChatMessageRepository messageRepository;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    // client gửi tin nhắn tới admin
    @MessageMapping("/chat.private")
    public void sendPrivateMessage(ChatMessage message) {
        // Bắt buộc phải có sender và receiver
        if (!StringUtils.hasText(message.getSender()) || !StringUtils.hasText(message.getReceiver())) {
            throw new IllegalArgumentException("Sender và receiver không được null");
        }

        // Lưu tin nhắn
        message.setTimeStamp(LocalDateTime.now());
        chatService.saveMessage(message);
        System.out.println("Received message: " + message.getSender() + " -> " + message.getReceiver());


        // Gửi tin nhắn tới receiver
        messagingTemplate.convertAndSendToUser(
                message.getReceiver(),   // người nhận (admin hoặc client)
                "/queue/messages",
                message
        );
    }

    @GetMapping("/api/chat/history")
    public List<ChatMessage> getHistory(@RequestParam String user,
                                        @RequestParam String other) {
        if (!StringUtils.hasText(user) || !StringUtils.hasText(other)) {
            throw new IllegalArgumentException("user và other không được rỗng");
        }
        List<ChatMessage> tesst = chatService.getChatHistory(user, other);
        for (ChatMessage message : tesst) {
            System.out.println("user: " + user+ " - admin : "+other+" :: content: "+message.getContent());
        }
        return chatService.getChatHistory(user, other);
    }
    @GetMapping("/api/chat/clients")
    public List<String> getClients() {
        // Lấy tất cả sender đã chat với admin
        return chatService.getAllClientsForAdmin("minhAdmin");
    }
    // 🔹 Khi gửi tin nhắn qua API REST (ví dụ khi ấn “Ứng tuyển ngay”)
    @PostMapping("/api/chat/send")
    public void sendMessageFromHttp(@RequestBody ChatMessage message) {

        // save message
        message.setTimeStamp(LocalDateTime.now());
        chatService.saveMessage(message);
        System.out.println("Received message: " + message.getSender() + " -> " + message.getReceiver());

        simpMessagingTemplate.convertAndSendToUser(
                message.getReceiver(),
                "/queue/messages",
                message
        );
    }
}



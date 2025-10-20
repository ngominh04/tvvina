package com.tvvina.tvvina.service;

import com.tvvina.tvvina.domain.ChatMessage;
import com.tvvina.tvvina.respository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    // Lưu tin nhắn vào database
    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

    // Lấy lịch sử chat giữa 2 người
    public List<ChatMessage> getChatHistory(String user1, String user2) {
        List<ChatMessage> list = chatMessageRepository.findChatHistory(user1, user2);
        return list != null ? list : new ArrayList<>();
    }

    // Lấy danh sách client đã từng chat với admin
    public List<String> getAllClientsForAdmin(String adminUsername) {
        List<ChatMessage> messages = chatMessageRepository.findBySenderOrReceiver(adminUsername, adminUsername);
        return messages.stream()
                .map(m -> m.getSender().equals(adminUsername) ? m.getReceiver() : m.getSender())
                .distinct()
                .collect(Collectors.toList());
    }
    // Đánh dấu tất cả tin nhắn chưa đọc của user là đã đọc
    public int markMessagesAsRead(String username) {
        return chatMessageRepository.markAsRead(username);
    }
}



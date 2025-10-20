package com.tvvina.tvvina.respository;

import com.tvvina.tvvina.domain.ChatMessage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    // Lấy lịch sử chat giữa 2 user theo thời gian tăng dần
//    @Query(value = "select * from chat_message\n" +
//            "    where sender=? and receiver =?\n" +
//            "    order by time_stamp",nativeQuery = true)
    @Query(value = "SELECT * FROM chat_message " +
            "WHERE (sender = :user1 AND receiver = :user2) " +
            "   OR (sender = :user2 AND receiver = :user1) " +
            "ORDER BY time_stamp", nativeQuery = true)
    List<ChatMessage> findChatHistory(@Param("user1") String user1,
                                      @Param("user2") String user2);
    List<ChatMessage> findBySenderOrReceiver(String user, String user1);

    long countByReceiverAndIsReadFalse(String receiver);
    // Cập nhật tất cả tin nhắn chưa đọc thành đã đọc
    @Modifying
    @Transactional
    @Query(value = "UPDATE chat_message c SET c.is_read = true WHERE c.receiver = :receiver AND c.is_read = false",nativeQuery = true)
    int markAsRead(@Param("receiver") String receiver);
}


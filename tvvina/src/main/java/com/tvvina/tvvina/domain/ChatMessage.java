package com.tvvina.tvvina.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "sender", length = 250)
    private String sender;

    @Column(name = "receiver", length = 250)
    private String receiver;

    @Column(name = "content", length = 550)
    private String content;

    @Column(name = "time_stamp")
    private LocalDateTime timeStamp;

    @Column(name = "is_read")
    private Boolean isRead;

}
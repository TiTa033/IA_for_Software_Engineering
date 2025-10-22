package com.example.pfa.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "contacts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String subject;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String comments;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "ai_summary", columnDefinition = "TEXT")
    private String aiSummary;
    
    @Column(name = "suggested_reply", columnDefinition = "TEXT")
    private String suggestedReply;
    
    @Column(name = "message_category")
    private String messageCategory;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

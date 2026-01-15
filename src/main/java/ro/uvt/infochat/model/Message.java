package ro.uvt.infochat.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conversation_id", nullable = false)
    @NotNull(message = "Conversation ID is required")
    private Long conversationId;

    @Column(name = "sender_type", nullable = false)
    @NotBlank(message = "Sender type is required")
    @Pattern(regexp = "USER|AI", message = "Sender type must be USER or AI")
    private String senderType;

    @Column(name = "message_text", nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Message text cannot be empty")
    private String messageText;

    private LocalDateTime timestamp;

    public Message() {
        this.timestamp = LocalDateTime.now();
    }

    public Message(Long conversationId, String senderType, String messageText) {
        this.conversationId = conversationId;
        this.senderType = senderType;
        this.messageText = messageText;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getConversationId() { return conversationId; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }

    public String getSenderType() { return senderType; }
    public void setSenderType(String senderType) { this.senderType = senderType; }

    public String getMessageText() { return messageText; }
    public void setMessageText(String messageText) { this.messageText = messageText; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
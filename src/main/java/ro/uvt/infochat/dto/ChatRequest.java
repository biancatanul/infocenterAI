package ro.uvt.infochat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ChatRequest {

    @NotBlank(message = "Message cannot be empty")
    private String message;

    private Long conversationId;

    @NotNull(message = "User ID is required")
    private Long userId;
    public ChatRequest() {}

    public ChatRequest(String message, Long conversationId, Long userId) {
        this.message = message;
        this.conversationId = conversationId;
        this.userId = userId;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getConversationId() { return conversationId; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
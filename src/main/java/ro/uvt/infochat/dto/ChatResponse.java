package ro.uvt.infochat.dto;

public class ChatResponse {
    private Long conversationId;
    private Long messageId;
    private String output;
    private boolean success;
    private String error;

    public ChatResponse() {}

    public ChatResponse(Long conversationId, Long messageId, String output, boolean success) {
        this.conversationId = conversationId;
        this.messageId = messageId;
        this.output = output;
        this.success = success;
    }

    public Long getConversationId() { return conversationId; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }

    public Long getMessageId() { return messageId; }
    public void setMessageId(Long messageId) { this.messageId = messageId; }

    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
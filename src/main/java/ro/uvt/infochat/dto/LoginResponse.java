package ro.uvt.infochat.dto;

public class LoginResponse {
    private boolean success;
    private Long userId;
    private String message;

    public LoginResponse() {}

    public LoginResponse(boolean success, Long userId, String message) {
        this.success = success;
        this.userId = userId;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
package ro.uvt.infochat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.uvt.infochat.dto.ChatRequest;
import ro.uvt.infochat.dto.ChatResponse;
import ro.uvt.infochat.model.Message;
import ro.uvt.infochat.service.ChatService;
import ro.uvt.infochat.repository.MessageRepository;
import ro.uvt.infochat.model.Conversation;
import ro.uvt.infochat.repository.ConversationRepository;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    public ChatController(ChatService chatService, MessageRepository messageRepository, ConversationRepository conversationRepository) {
        this.chatService = chatService;
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
    }

    @PostMapping("/send")
    public ResponseEntity<ChatResponse> sendMessage(@Valid @RequestBody ChatRequest request) {
        ChatResponse response = chatService.processMessage(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/messages/{conversationId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Long conversationId) {
        List<Message> messages = messageRepository.findByConversationIdOrderByTimestampAsc(conversationId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/conversations/{userId}")
    public ResponseEntity<List<Conversation>> getConversations(@PathVariable Long userId) {
        List<Conversation> conversations = conversationRepository.findByUserIdOrderByUpdatedAtDesc(userId);
        return ResponseEntity.ok(conversations);
    }

    @DeleteMapping("/conversation/{conversationId}")
    public ResponseEntity<Map<String, Object>> deleteConversation(@PathVariable Long conversationId) {
        Map<String, Object> response = new HashMap<>();

        try {
            conversationRepository.deleteById(conversationId);
            response.put("success", true);
            response.put("message", "Conversation deleted");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to delete conversation");
            return ResponseEntity.ok(response);
        }
    }
}
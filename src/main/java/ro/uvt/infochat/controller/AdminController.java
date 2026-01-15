package ro.uvt.infochat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.uvt.infochat.model.User;
import ro.uvt.infochat.model.Conversation;
import ro.uvt.infochat.repository.UserRepository;
import ro.uvt.infochat.repository.ConversationRepository;
import ro.uvt.infochat.repository.MessageRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public AdminController(UserRepository userRepository,
                           ConversationRepository conversationRepository,
                           MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();

        List<User> allUsers = userRepository.findAll();
        long studentCount = allUsers.stream().filter(u -> "STUDENT".equals(u.getRole())).count();
        long adminCount = allUsers.stream().filter(u -> "ADMIN".equals(u.getRole())).count();
        long conversationCount = conversationRepository.count();

        stats.put("studentCount", studentCount);
        stats.put("adminCount", adminCount);
        stats.put("conversationCount", conversationCount);

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<Map<String, Object>> userList = users.stream().map(user -> {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("email", user.getEmail());
            userMap.put("role", user.getRole());

            long convCount = conversationRepository.countByUserId(user.getId());
            userMap.put("conversationCount", convCount);

            return userMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(userList);
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<Map<String, Object>>> getAllConversations() {
        List<Conversation> conversations = conversationRepository.findAllByOrderByUpdatedAtDesc();

        List<Map<String, Object>> convList = conversations.stream().map(conv -> {
            Map<String, Object> convMap = new HashMap<>();
            convMap.put("id", conv.getId());
            convMap.put("userId", conv.getUserId());

            userRepository.findById(conv.getUserId()).ifPresent(user -> {
                convMap.put("userEmail", user.getEmail());
            });

            convMap.put("title", conv.getTitle());
            convMap.put("updatedAt", conv.getUpdatedAt());

            long msgCount = messageRepository.countByConversationId(conv.getId());
            convMap.put("messageCount", msgCount);

            return convMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(convList);
    }
}
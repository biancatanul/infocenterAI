package ro.uvt.infochat.service;

import org.springframework.stereotype.Service;
import ro.uvt.infochat.dto.ChatRequest;
import ro.uvt.infochat.dto.ChatResponse;
import ro.uvt.infochat.model.Conversation;
import ro.uvt.infochat.model.Message;
import ro.uvt.infochat.repository.ConversationRepository;
import ro.uvt.infochat.repository.MessageRepository;

import java.time.LocalDateTime;

@Service
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final AIService aiService;

    public ChatService(ConversationRepository conversationRepository,
                       MessageRepository messageRepository,
                       AIService aiService) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.aiService = aiService;
    }

    public ChatResponse processMessage(ChatRequest request) {
        try {
            Conversation conversation;
            if (request.getConversationId() != null) {
                conversation = conversationRepository.findById(request.getConversationId())
                        .orElseThrow(() -> new RuntimeException("Conversation not found"));
            } else {
                String title = generateTitle(request.getMessage());
                conversation = new Conversation(request.getUserId(), title);
                conversation = conversationRepository.save(conversation);
            }

            Message userMessage = new Message(conversation.getId(), "USER", request.getMessage());
            messageRepository.save(userMessage);

            String aiOutput = aiService.generateResponse(request.getMessage());

            Message aiMessage = new Message(conversation.getId(), "AI", aiOutput);
            aiMessage = messageRepository.save(aiMessage);

            conversation.setUpdatedAt(LocalDateTime.now());
            conversationRepository.save(conversation);

            return new ChatResponse(conversation.getId(), aiMessage.getId(), aiOutput, true);

        } catch (Exception e) {
            ChatResponse errorResponse = new ChatResponse();
            errorResponse.setSuccess(false);
            errorResponse.setError(e.getMessage());
            return errorResponse;
        }
    }
    private String generateTitle(String message) {
        if (message.length() <= 50) {
            return message;
        }
        return message.substring(0, 47) + "...";
    }
}
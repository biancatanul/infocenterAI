package ro.uvt.infochat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.uvt.infochat.model.Conversation;
import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByUserIdOrderByUpdatedAtDesc(Long userId);
    long countByUserId(Long userId);
    List<Conversation> findAllByOrderByUpdatedAtDesc();
}
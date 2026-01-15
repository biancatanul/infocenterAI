package ro.uvt.infochat.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class ConversationTest {

    // Constructor tests

    @Test
    public void testDefaultConstructor() {
        Conversation conv = new Conversation();
        assertNotNull(conv);
        assertNotNull(conv.getCreatedAt());
        assertNotNull(conv.getUpdatedAt());
    }

    @Test
    public void testParameterizedConstructor() {
        Conversation conv = new Conversation(1L, "Test chat");

        assertEquals(1L, conv.getUserId());
        assertEquals("Test chat", conv.getTitle());
        assertNotNull(conv.getCreatedAt());
    }

    @Test
    public void testConstructorSetsTimestamps() {
        Conversation conv = new Conversation();
        LocalDateTime now = LocalDateTime.now();

        assertTrue(conv.getCreatedAt().isBefore(now.plusSeconds(1)));
        assertTrue(conv.getUpdatedAt().isBefore(now.plusSeconds(1)));
    }

    // Functionality tests

    @Test
    public void testSetAndGetUserId() {
        Conversation conv = new Conversation();
        conv.setUserId(10L);
        assertEquals(10L, conv.getUserId());
    }

    @Test
    public void testSetAndGetTitle() {
        Conversation conv = new Conversation();
        conv.setTitle("My first conversation");
        assertEquals("My first conversation", conv.getTitle());
    }

    @Test
    public void testUpdateTimestamp() {
        Conversation conv = new Conversation();
        LocalDateTime original = conv.getUpdatedAt();

        conv.setUpdatedAt(LocalDateTime.now().plusHours(1));
        assertTrue(conv.getUpdatedAt().isAfter(original));
    }

    @Test
    public void testSetAndGetId() {
        Conversation conv = new Conversation();
        conv.setId(3L);
        assertEquals(3L, conv.getId());
    }

    @Test
    public void testCreatedAtIsBeforeUpdatedAt() {
        Conversation conv = new Conversation();
        assertTrue(conv.getCreatedAt().isBefore(conv.getUpdatedAt()) ||
                conv.getCreatedAt().equals(conv.getUpdatedAt()));
    }
}
package ro.uvt.infochat.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    // Constructor tests:
    @Test
    public void testDefaultConstructor() {
        Message msg = new Message();
        assertNotNull(msg);
        assertNotNull(msg.getTimestamp());
    }

    @Test
    public void testParameterizedConstructor() {
        Message msg = new Message(1L, "USER", "Hello");

        assertEquals(1L, msg.getConversationId());
        assertEquals("USER", msg.getSenderType());
        assertEquals("Hello", msg.getMessageText());
    }

    @Test
    public void testConstructorSetsTimestamp() {
        Message msg = new Message();
        LocalDateTime now = LocalDateTime.now();

        assertTrue(msg.getTimestamp().isBefore(now.plusSeconds(1)));
    }
    // Functionality tests:
    @Test
    public void testSetAndGetConversationId() {
        Message msg = new Message();
        msg.setConversationId(5L);
        assertEquals(5L, msg.getConversationId());
    }

    @Test
    public void testSetAndGetSenderType() {
        Message msg = new Message();
        msg.setSenderType("AI");
        assertEquals("AI", msg.getSenderType());
    }

    @Test
    public void testSetAndGetMessageText() {
        Message msg = new Message();
        msg.setMessageText("Test message");
        assertEquals("Test message", msg.getMessageText());
    }

    @Test
    public void testSenderTypeValidation() {
        Message msg = new Message();
        msg.setSenderType("USER");
        assertTrue(msg.getSenderType().equals("USER") || msg.getSenderType().equals("AI"));
    }

    @Test
    public void testMessageTextNotEmpty() {
        Message msg = new Message(1L, "USER", "Non-empty");
        assertFalse(msg.getMessageText().isEmpty());
    }
}
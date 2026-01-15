package ro.uvt.infochat.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    // Constructor tests:
    @Test
    public void testDefaultConstructor() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    public void testParameterizedConstructor() {
        User user = new User(1L, "test@email.com", "password", "STUDENT");

        assertEquals(1L, user.getId());
        assertEquals("test@email.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals("STUDENT", user.getRole());
    }

    @Test
    public void testConstructorWithNullValues() {
        User user = new User(null, null, null, null);
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getEmail());
    }

    // Functionality tests:
    @Test
    public void testSetAndGetEmail() {
        User user = new User();
        user.setEmail("student@e-uvt.ro");
        assertEquals("student@e-uvt.ro", user.getEmail());
    }

    @Test
    public void testSetAndGetPassword() {
        User user = new User();
        user.setPassword("securePass");
        assertEquals("securePass", user.getPassword());
    }

    @Test
    public void testSetAndGetRole() {
        User user = new User();
        user.setRole("ADMIN");
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    public void testSetAndGetId() {
        User user = new User();
        user.setId(5L);
        assertEquals(5L, user.getId());
    }

    @Test
    public void testRoleValidation() {
        User user = new User();
        user.setRole("STUDENT");
        assertTrue(user.getRole().equals("STUDENT") || user.getRole().equals("ADMIN"));
    }
}
package com.heg;

import com.heg.entity.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    // ─── User Entity Tests (covers User.java completely) ─────────────────

    @Test
    public void testUserNoArgsConstructor() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    public void testUserParameterizedConstructor() {
        User user = new User(1L, "Anchit", "anchit@test.com");
        assertEquals(1L, user.getId());
        assertEquals("Anchit", user.getName());
        assertEquals("anchit@test.com", user.getEmail());
    }

    @Test
    public void testUserSettersAndGetters() {
        User user = new User();
        user.setId(2L);
        user.setName("Pradeep");
        user.setEmail("pradeep@test.com");

        assertEquals(2L, user.getId());
        assertEquals("Pradeep", user.getName());
        assertEquals("pradeep@test.com", user.getEmail());
    }

    @Test
    public void testUserIdSetterGetter() {
        User user = new User();
        user.setId(100L);
        assertEquals(100L, user.getId());
    }

    @Test
    public void testUserNameSetterGetter() {
        User user = new User();
        user.setName("TestUser");
        assertEquals("TestUser", user.getName());
    }

    @Test
    public void testUserEmailSetterGetter() {
        User user = new User();
        user.setEmail("test@example.com");
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    public void testUserNullValues() {
        User user = new User();
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
    }

    @Test
    public void testUserSerializable() {
        User user = new User(1L, "Test", "test@test.com");
        assertInstanceOf(java.io.Serializable.class, user);
    }
}
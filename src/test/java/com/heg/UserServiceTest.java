package com.heg;

import com.heg.entity.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {    // ← removed public

    @Test
    void testUserNoArgsConstructor() {    // ← removed public
        User user = new User();
        assertNotNull(user);
    }

    @Test
    void testUserParameterizedConstructor() {    // ← removed public
        User user = new User(1L, "Anchit", "anchit@test.com");
        assertEquals(1L, user.getId());
        assertEquals("Anchit", user.getName());
        assertEquals("anchit@test.com", user.getEmail());
    }

    @Test
    void testUserSettersAndGetters() {    // ← removed public
        User user = new User();
        user.setId(2L);
        user.setName("Pradeep");
        user.setEmail("pradeep@test.com");

        assertEquals(2L, user.getId());
        assertEquals("Pradeep", user.getName());
        assertEquals("pradeep@test.com", user.getEmail());
    }

    @Test
    void testUserIdSetterGetter() {    // ← removed public
        User user = new User();
        user.setId(100L);
        assertEquals(100L, user.getId());
    }

    @Test
    void testUserNameSetterGetter() {    // ← removed public
        User user = new User();
        user.setName("TestUser");
        assertEquals("TestUser", user.getName());
    }

    @Test
    void testUserEmailSetterGetter() {    // ← removed public
        User user = new User();
        user.setEmail("test@example.com");
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testUserNullValues() {    // ← removed public
        User user = new User();
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
    }

    @Test
    void testUserSerializable() {    // ← removed public
        User user = new User(1L, "Test", "test@test.com");
        assertInstanceOf(java.io.Serializable.class, user);
    }
}
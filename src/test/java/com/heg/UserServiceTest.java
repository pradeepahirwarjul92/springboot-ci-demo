package com.heg;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import java.util.List;

// Importing your specific Controller and Entity
import com.heg.controller.UserController;
import com.heg.entity.User;

public class UserServiceTest {

    // Direct instantiation for the test
    private final UserController userController = new UserController();

    @Test
    public void testUserListCoverage() {
        // Explicitly defining the type as List<User> to fix the 'size()' error
        List<User> users = userController.getUsers(); 
        
        assertNotNull(users);
        
        // This line now knows what .size() is because we defined 'users' as a List
        assertTrue(users.size() >= 19);
    }
}
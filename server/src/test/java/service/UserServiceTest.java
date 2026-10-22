package service;

import dataaccess.*;
import org.junit.jupiter.api.*;

public class UserServiceTest {
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
    }

    @Test
    @DisplayName("Register success")
    public void registerPositive() throws Exception {
        RegisterRequest request = new RegisterRequest("Luke", "pwd", "a@b.com");
        RegisterResult result = userService.register(request);
        Assertions.assertEquals("Luke", result.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    @DisplayName("Register duplicate username")
    public void registerNegative() throws Exception {
        RegisterRequest request = new RegisterRequest("Luke", "pwd", "a@b.com");
        userService.register(request);
        Assertions.assertThrows(AlreadyTakenException.class, () -> userService.register(request));
    }
}
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

    @Test
    @DisplayName("Login Success")
    public void loginPositive() throws Exception {
        userService.register(new RegisterRequest("Luke", "pwd", "a@b.com"));

        LoginRequest request = new LoginRequest("Luke", "pwd");
        LoginResult result = userService.login(request);
        Assertions.assertEquals("Luke", result.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    @DisplayName("Login Wrong Password")
    public void loginNegative() throws Exception {
        userService.register(new RegisterRequest("Luke", "pwd", "a@b.com"));

        LoginRequest request = new LoginRequest("Luke", "123");
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.login(request));
    }

    @Test
    @DisplayName("Logout Success")
    public void logoutPositive() throws Exception {
        RegisterResult result = userService.register(new RegisterRequest("Luke", "pwd", "a@b.com"));
        userService.logout(result.authToken());
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.logout(result.authToken()));
    }

    @Test
    @DisplayName("Logout Nonexistent Token")
    public void logoutNegative() throws Exception {
        String authToken = "bad-token";
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.logout(authToken));
    }
}
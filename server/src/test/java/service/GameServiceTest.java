package service;

import dataaccess.*;
import org.junit.jupiter.api.*;

public class GameServiceTest {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private GameService gameService;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        gameService = new GameService(gameDAO, authDAO);
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    @DisplayName("Create Game Success")
    public void createGamePositive() throws Exception {
        RegisterResult result = userService.register(new RegisterRequest("Luke", "pwd", "a@b.com"));
        CreateGameResult gameResult = gameService.createGame(result.authToken(), new CreateGameRequest("game1"));
        Assertions.assertEquals(1, gameResult.gameID());
        Assertions.assertNotNull(gameDAO.getGame(gameResult.gameID()));
    }

    @Test
    @DisplayName("Create Game Bad Auth Token")
    public void createGameNegative() throws Exception {
        String authToken = "bad-token";
        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.createGame(authToken, new CreateGameRequest("game1")));
    }
}

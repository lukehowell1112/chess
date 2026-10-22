package service;

import dataaccess.*;
import model.GameData;
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

    @Test
    @DisplayName("Create Game Bad Request")
    public void createGameBadRequest() throws Exception {
        RegisterResult result = userService.register(new RegisterRequest("Luke", "pwd", "a@b.com"));
        Assertions.assertThrows(BadRequestException.class, () -> gameService.createGame(result.authToken(), new CreateGameRequest(null)));
    }

    @Test
    @DisplayName("List Games Success")
    public void listGamesPositive() throws Exception {
        RegisterResult authResult = userService.register(new RegisterRequest("Luke", "pwd", "a@b.com"));
        gameService.createGame(authResult.authToken(), new CreateGameRequest("game1"));
        gameService.createGame(authResult.authToken(), new CreateGameRequest("game2"));
        ListGamesResult listResult = gameService.listGames(authResult.authToken());
        Assertions.assertEquals(2, listResult.games().size());
    }

    @Test
    @DisplayName("List Games Bad Auth Token")
    public void listGamesNegative() throws Exception {
        String authToken = "bad-token";
        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.listGames(authToken));
    }

    @Test
    @DisplayName("Join Game Success")
    public void joinGamePositive() throws Exception {
        RegisterResult authResult1 = userService.register(new RegisterRequest("Luke", "pwd", "a@b.com"));
        RegisterResult authResult2 = userService.register(new RegisterRequest("Luke2", "pwd2", "a@b2.com"));
        CreateGameResult gameResult = gameService.createGame(authResult1.authToken(), new CreateGameRequest("game1"));
        gameService.joinGame(authResult1.authToken(), new JoinGameRequest("WHITE", gameResult.gameID()));
        gameService.joinGame(authResult2.authToken(), new JoinGameRequest("BLACK", gameResult.gameID()));

        GameData gameData = gameDAO.getGame(gameResult.gameID());
        Assertions.assertEquals("Luke", gameData.whiteUsername());
        Assertions.assertEquals("Luke2", gameData.blackUsername());
    }

    @Test
    @DisplayName("Two Users Join Game As White")
    public void joinGameNegative() throws Exception {
        RegisterResult authResult1 = userService.register(new RegisterRequest("Luke", "pwd", "a@b.com"));
        RegisterResult authResult2 = userService.register(new RegisterRequest("Luke2", "pwd2", "a@b2.com"));
        CreateGameResult gameResult = gameService.createGame(authResult1.authToken(), new CreateGameRequest("game1"));
        gameService.joinGame(authResult1.authToken(), new JoinGameRequest("WHITE", gameResult.gameID()));

        Assertions.assertThrows(AlreadyTakenException.class, () ->
                gameService.joinGame(authResult2.authToken(), new JoinGameRequest("WHITE", gameResult.gameID())));
    }
}

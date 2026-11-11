package client;

import org.junit.jupiter.api.*;
import server.Server;

import java.util.List;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade(port);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void clearDatabase() throws ResponseException {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    void registerPositive() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        Assertions.assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerNegative() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        var ex = Assertions.assertThrows(ResponseException.class,
                () -> facade.register("player1", "password", "p1@email.com"));
        Assertions.assertEquals(403, ex.getStatusCode());
    }

    @Test
    void loginPositive() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        var authData = facade.login("player1", "password");
        Assertions.assertEquals("player1", authData.username());
        Assertions.assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void loginNegative() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        var authData = facade.login("player1", "password");
        Assertions.assertTrue(authData.authToken().length() > 10);
        var ex = Assertions.assertThrows(ResponseException.class,
                () -> facade.login("player1", "pwd"));
        Assertions.assertEquals(401, ex.getStatusCode());
    }

    @Test
    void logoutPositive() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        var authData = facade.login("player1", "password");
        Assertions.assertDoesNotThrow(() -> facade.logout(authData.authToken()));
        var ex = Assertions.assertThrows(ResponseException.class, () -> facade.logout(authData.authToken()));
        Assertions.assertEquals(401, ex.getStatusCode());
    }

    @Test
    void logoutNegative() throws Exception {
        var ex = Assertions.assertThrows(ResponseException.class,
                () -> facade.logout("fake-token"));
        Assertions.assertEquals(401, ex.getStatusCode());
    }

    @Test
    void createGamePositive() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        int gameID = facade.createGame(authData.authToken(), "game1");
        Assertions.assertTrue(gameID > 0);
    }

    @Test
    void createGameNegative() throws Exception {
        var ex = Assertions.assertThrows(ResponseException.class,
                () -> facade.createGame("fake-token", "game1"));
        Assertions.assertEquals(401, ex.getStatusCode());
    }

    @Test
    void listGamesPositive() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        facade.createGame(authData.authToken(), "game1");
        facade.createGame(authData.authToken(), "game2");
        List<GameSummary> games = facade.listGames(authData.authToken());
        Assertions.assertEquals(2, games.size());
        Assertions.assertEquals("game1", games.get(0).gameName());
        Assertions.assertEquals("game2", games.get(1).gameName());
    }

    @Test
    void listGamesNegative() throws Exception {
        var ex = Assertions.assertThrows(ResponseException.class,
                () -> facade.listGames("fake_token"));
        Assertions.assertEquals(401, ex.getStatusCode());
    }

    @Test
    void joinGamePositive() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        int gameID = facade.createGame(authData.authToken(), "game1");
        facade.joinGame(authData.authToken(), "WHITE", gameID);
        List<GameSummary> games = facade.listGames(authData.authToken());
        Assertions.assertEquals("player1", games.get(0).whiteUsername());
    }

    @Test
    void joinGameNegative() throws Exception {
        var authData1 = facade.register("player1", "password", "p1@email.com");
        var authData2 = facade.register("player2", "password", "p1@email.com");
        int gameID = facade.createGame(authData1.authToken(), "game1");
        facade.joinGame(authData1.authToken(), "WHITE", gameID);
        var ex = Assertions.assertThrows(ResponseException.class,
                () -> facade.joinGame(authData2.authToken(), "WHITE", gameID));
        Assertions.assertEquals(403, ex.getStatusCode());
    }
}

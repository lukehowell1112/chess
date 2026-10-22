package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

public class ClearServiceTest {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private ClearService clearService;

    @BeforeEach
    public void setUp() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        clearService = new ClearService(userDAO, authDAO, gameDAO);
    }

    @Test
    @DisplayName("Clear Success")
    public void clearPositive() throws Exception {
        userDAO.createUser(new UserData("Luke", "pwd", "a@b.com"));
        authDAO.createAuth(new AuthData("token", "Luke"));
        gameDAO.createGame("test");

        clearService.clear();

        Assertions.assertNull(userDAO.getUser("Luke"));
        Assertions.assertNull(authDAO.getAuth("token"));
        Assertions.assertTrue(gameDAO.listGames().isEmpty());
    }

    @Test
    @DisplayName("Clear Fails When DAO Fails")
    public void clearNegative() {
        UserDAO brokenUserDAO = new MemoryUserDAO() {
            @Override
            public void clear() throws DataAccessException {
                throw new DataAccessException("Error: database failure");
            }
        };
        ClearService brokenService = new ClearService(brokenUserDAO, authDAO, gameDAO);

        Assertions.assertThrows(DataAccessException.class, brokenService::clear);
    }
}

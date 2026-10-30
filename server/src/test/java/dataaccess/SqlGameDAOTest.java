package dataaccess;

import chess.ChessGame;
import dataaccess.sql.DatabaseSetup;
import dataaccess.sql.SqlGameDAO;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class SqlGameDAOTest {
    private SqlGameDAO gameDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        DatabaseSetup.createTables();
        gameDAO = new SqlGameDAO();
        gameDAO.clear();
    }

    @Test
    @DisplayName("Create Game Success")
    public void createGamePositive() throws DataAccessException {
        int gameID1 = gameDAO.createGame("game1");
        Assertions.assertTrue(gameID1 > 0);
        int gameID2 = gameDAO.createGame("game2");
        Assertions.assertNotEquals(gameID1, gameID2);
    }

    @Test
    @DisplayName("Null Game Name")
    public void createGameNegative() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }

    @Test
    @DisplayName("Get Game Success")
    public void getGamePositive() throws DataAccessException {
        int gameID1 = gameDAO.createGame("game1");
        GameData data1 = gameDAO.getGame(gameID1);
        Assertions.assertEquals("game1", data1.gameName());
        Assertions.assertNotNull(data1.game());
        Assertions.assertEquals(new ChessGame(), data1.game());
    }

    @Test
    @DisplayName("Get Game with Nonexistent ID")
    public void getGameNegative() throws DataAccessException {
        Assertions.assertNull(gameDAO.getGame(99999));
    }

    @Test
    @DisplayName("List Games Success")
    public void listGamesPositive() throws DataAccessException {
        gameDAO.createGame("game1");
        gameDAO.createGame("game2");
        gameDAO.createGame("game3");
        Collection<GameData> games = gameDAO.listGames();
        Assertions.assertEquals(3, games.size());
    }

    @Test
    @DisplayName("Create an Empty Game List")
    public void listGamesNegative() throws DataAccessException {
        Collection<GameData> games = gameDAO.listGames();
        Assertions.assertTrue(games.isEmpty());
    }

    @Test
    @DisplayName("Update Game Success")
    public void updateGamePositive() throws DataAccessException {
        int gameID1 = gameDAO.createGame("game1");
        GameData data1 = gameDAO.getGame(gameID1);
        GameData updated1 = new GameData(gameID1, "Luke", null, data1.gameName(), data1.game());
        gameDAO.updateGame(updated1);
        GameData updatedData1 = gameDAO.getGame(gameID1);
        Assertions.assertEquals("Luke", updatedData1.whiteUsername());
    }

    @Test
    @DisplayName("Update Nonexistent Game")
    public void updateGameNegative() throws DataAccessException {
        GameData missing = new GameData(99999, null, null, "ghost", new ChessGame());
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGame(missing));
    }
}

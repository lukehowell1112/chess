package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import java.sql.SQLException;

public class DatabaseSetup {
    private static final String[] CREATE_STATEMENTS = {
            """
                CREATE TABLE IF NOT EXISTS user (
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                PRIMARY KEY (username)
            )
            """,
            """
                CREATE TABLE IF NOT EXISTS auth (
                authToken VARCHAR(36) NOT NULL,
                username VARCHAR(255) NOT NULL,
                PRIMARY KEY (authToken),
                FOREIGN KEY (username) REFERENCES user(username)
            )
            """,
            """
                CREATE TABLE IF NOT EXISTS game (
                gameID INT AUTO_INCREMENT NOT NULL,
                whiteUsername VARCHAR(255),
                blackUsername VARCHAR(255),
                gameName VARCHAR(255) NOT NULL,
                game TEXT NOT NULL,
                PRIMARY KEY (gameID)
            )
            """
    };

    public static void createTables() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (String statement : CREATE_STATEMENTS) {
                try (var ps = conn.prepareStatement(statement)) {
                    ps.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create tables", ex);
        }
    }
}
package dataaccess;

import dataaccess.sql.DatabaseSetup;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import dataaccess.sql.SqlUserDAO;

public class SqlUserDAOTest {
    private SqlUserDAO userDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        DatabaseSetup.createTables();
        userDAO = new SqlUserDAO();
        userDAO.clear();
    }

    @Test
    @DisplayName("Create User Success")
    public void createUserPositive() throws Exception {
        userDAO.createUser(new UserData("Luke", "pwd", "a@b.com"));
        UserData stored = userDAO.getUser("Luke");
        Assertions.assertEquals("Luke", stored.username());
        Assertions.assertEquals("a@b.com", stored.email());

    }
}

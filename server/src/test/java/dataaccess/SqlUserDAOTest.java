package dataaccess;

import dataaccess.sql.DatabaseSetup;
import dataaccess.sql.SqlUserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    @Test
    @DisplayName("Creating Two Users With the Same Name")
    public void createUserNegative() throws Exception {
        userDAO.createUser(new UserData("Luke", "pwd", "a@b.com"));
        Assertions.assertThrows(DataAccessException.class,
                () -> userDAO.createUser(new UserData("Luke", "pwd", "a@b.com")));
    }

    @Test
    @DisplayName("Get User Success")
    public void getUserPositive() throws Exception {
        userDAO.createUser(new UserData("Luke", "pwd", "a@b.com"));
        userDAO.createUser(new UserData("Cosmo", "byu", "cosmo@byu.edu"));
        UserData stored1 = userDAO.getUser("Luke");
        UserData stored2 = userDAO.getUser("Cosmo");
        Assertions.assertEquals("Luke", stored1.username());
        Assertions.assertEquals("Cosmo", stored2.username());
        Assertions.assertEquals("a@b.com", stored1.email());
        Assertions.assertEquals("cosmo@byu.edu", stored2.email());
    }

    @Test
    @DisplayName("Get User That Does Not Exist")
    public void getUserNegative() throws Exception {
        userDAO.createUser(new UserData("Luke", "pwd", "a@b.com"));
        Assertions.assertNull(userDAO.getUser("Cosmo"));
    }

    @Test
    @DisplayName("Clear Removes Users")
    public void clearTest() throws Exception {
        userDAO.createUser(new UserData("Luke", "pwd", "a@b.com"));
        userDAO.clear();
        Assertions.assertNull(userDAO.getUser("Luke"));
    }
}

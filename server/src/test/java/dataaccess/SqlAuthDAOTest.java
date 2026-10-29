package dataaccess;

import dataaccess.sql.DatabaseSetup;
import dataaccess.sql.SqlAuthDAO;
import dataaccess.sql.SqlUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SqlAuthDAOTest {
    private SqlAuthDAO authDAO;
    private SqlUserDAO userDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        DatabaseSetup.createTables();
        authDAO = new SqlAuthDAO();
        userDAO = new SqlUserDAO();
        authDAO.clear();
        userDAO.clear();
    }

    @Test
    @DisplayName("Create Auth Success")
    public void createAuthPositive() throws Exception {
        userDAO.createUser(new UserData("Luke", "pwd", "a@b.com"));
        authDAO.createAuth(new AuthData("abc", "Luke"));
        AuthData stored = authDAO.getAuth("abc");
        Assertions.assertEquals("Luke", stored.username());
        Assertions.assertEquals("abc", stored.authToken());
    }

    @Test
    @DisplayName("Create Auth For User that Does Not Exist")
    public void createAuthNegative() throws Exception {
        Assertions.assertThrows(DataAccessException.class,
                () -> authDAO.createAuth(new AuthData("abc", "Luke")));
    }

    @Test
    @DisplayName("Get Auth Success")
    public void getAuthPositive() throws Exception {
        userDAO.createUser(new UserData("Luke", "pwd", "a@b.com"));
        userDAO.createUser(new UserData("Cosmo", "byu", "cosmo@byu.edu"));
        authDAO.createAuth(new AuthData("abc", "Luke"));
        authDAO.createAuth(new AuthData("123", "Cosmo"));
        AuthData stored1 = authDAO.getAuth("abc");
        AuthData stored2 = authDAO.getAuth("123");
        Assertions.assertEquals("Luke", stored1.username());
        Assertions.assertEquals("Cosmo", stored2.username());
        Assertions.assertEquals("abc", stored1.authToken());
        Assertions.assertEquals("123", stored2.authToken());
    }

    @Test
    @DisplayName("Ask For Nonexistent Token")
    public void getAuthNegative() throws Exception {
        userDAO.createUser(new UserData("Luke", "pwd", "a@b.com"));
        authDAO.createAuth(new AuthData("abc", "Luke"));
        Assertions.assertNull(authDAO.getAuth("bad-token"));
    }

    @Test
    @DisplayName("Delete Auth Success")
    public void deleteAuthPositive() throws Exception {
        userDAO.createUser(new UserData("Luke", "pwd", "a@b.com"));
        authDAO.createAuth(new AuthData("abc", "Luke"));
        authDAO.deleteAuth("abc");
        Assertions.assertNull(authDAO.getAuth("abc"));
    }

    @Test
    @DisplayName("Delete Nonexistent Token")
    public void deleteAuthNegative() throws Exception {
        userDAO.createUser(new UserData("Luke", "pwd", "a@b.com"));
        authDAO.createAuth(new AuthData("abc", "Luke"));
        authDAO.deleteAuth("bad-token");
        AuthData stored = authDAO.getAuth("abc");
        Assertions.assertNotNull(stored);
        Assertions.assertEquals("abc", stored.authToken());
    }

    @Test
    @DisplayName("Clear Auth and User")
    public void clearTest() throws Exception {
        userDAO.createUser(new UserData("Luke", "pwd", "a@b.com"));
        authDAO.createAuth(new AuthData("abc", "Luke"));
        authDAO.clear();
        userDAO.clear();
        Assertions.assertNull(authDAO.getAuth("abc"));
        Assertions.assertNull(userDAO.getUser("Luke"));
    }
}

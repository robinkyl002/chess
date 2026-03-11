package dataaccess;

import exception.ResponseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private UserDAO getDataAccess(Class<? extends UserDAO> userDAOClass) throws ResponseException {
//        UserDAO db;
//        if(userDAOClass.equals(MemoryUserDAO.class)) {
//            db = new MemoryUserDAO();
//        }
//        return db;
        return null;
    }

    @Test
    void createUser() {
    }

    @Test
    void deleteUsers() {
    }

    @Test
    void getUser() {
    }
}
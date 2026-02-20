package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exception.ResponseException;

public class GameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void createGame() throws ResponseException {}
    public void joinGame() throws ResponseException {}
    public void listGames() throws ResponseException {}
    public void getGame() throws ResponseException {}
}

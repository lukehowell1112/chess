package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import java.util.ArrayList;
import java.util.Objects;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    private AuthData checkToken(String authToken) throws DataAccessException, UnauthorizedException {
        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return auth;
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest request) throws DataAccessException, UnauthorizedException, BadRequestException {
        checkToken(authToken);
        if (request.gameName() == null) {
            throw new BadRequestException("Error: bad request");
        }

        int gameID = gameDAO.createGame(request.gameName());
        return new CreateGameResult(gameID);
    }

    public ListGamesResult listGames(String authToken) throws DataAccessException, UnauthorizedException {
        checkToken(authToken);
        ArrayList<GameSummary> games = new ArrayList<>();
        for (GameData gameData : gameDAO.listGames()) {
            games.add(new GameSummary(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName()));
        }

        return new ListGamesResult(games);
    }

    public void joinGame(String authToken, JoinGameRequest request) throws DataAccessException, UnauthorizedException, BadRequestException, AlreadyTakenException {
        AuthData auth = checkToken(authToken);
        if (!"WHITE".equals(request.playerColor()) && !"BLACK".equals(request.playerColor())) {
            throw new BadRequestException("Error: bad request");
        }

        GameData game = gameDAO.getGame(request.gameID());
        if (game == null) {
            throw new BadRequestException("Error: bad request");
        }

        if (Objects.equals(request.playerColor(), "WHITE") && game.whiteUsername() != null) {
            throw new AlreadyTakenException("Error: already taken");
        }
        if (Objects.equals(request.playerColor(), "BLACK") && game.blackUsername() != null) {
            throw new AlreadyTakenException("Error: already taken");
        }

        GameData gameData;
        if (Objects.equals(request.playerColor(), "WHITE")) {
            gameData = new GameData(request.gameID(), auth.username(), game.blackUsername(), game.gameName(), game.game());
        } else {
            gameData = new GameData(request.gameID(), game.whiteUsername(), auth.username(), game.gameName(), game.game());
        }
        gameDAO.updateGame(gameData);
    }
}

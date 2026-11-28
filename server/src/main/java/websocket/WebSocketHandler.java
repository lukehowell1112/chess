package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import service.BadRequestException;
import service.GameService;
import service.UnauthorizedException;
import service.UserService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler {
    private final UserService userService;
    private final GameService gameService;
    private final ConnectionManager connectionManager;

    public WebSocketHandler(UserService userService, GameService gameService, ConnectionManager connectionManager) {
        this.userService = userService;
        this.gameService = gameService;
        this.connectionManager = connectionManager;
    }

    public void connect(UserGameCommand command, Session session) throws UnauthorizedException, DataAccessException, BadRequestException, IOException {
        AuthData authData = userService.checkAuth(command.getAuthToken());
        GameData gameData = gameService.checkGame(command.getGameID());
        connectionManager.add(command.getGameID(), session);

        connectionManager.messageOne(session, ServerMessage.loadGame(gameData.game()));
        String message;
        if (authData.username().equals(gameData.whiteUsername())) {
            message = authData.username() + " joined as white";
        } else if (authData.username().equals(gameData.blackUsername())) {
            message = authData.username() + " joined as black";
        } else {
            message = authData.username() + " is observing";
        }
        connectionManager.messageAllButOne(command.getGameID(), session, ServerMessage.notification(message));
    }

    public void move(MakeMoveCommand command, Session session) throws UnauthorizedException, DataAccessException, BadRequestException, IOException {
        AuthData authData = userService.checkAuth(command.getAuthToken());
        GameData gameData = gameService.checkGame(command.getGameID());
        ChessGame.TeamColor color;
        if (authData.username().equals(gameData.whiteUsername())) {
            color = ChessGame.TeamColor.WHITE;
        } else if (authData.username().equals(gameData.blackUsername())) {
            color = ChessGame.TeamColor.BLACK;
        } else {
            throw new BadRequestException("Error: observers cannot make moves");
        }

        if (gameData.game().isGameOver()) {
            throw new BadRequestException("Error: the game has ended");
        }

        ChessGame.TeamColor currentColor = gameData.game().getTeamTurn();
        if (!currentColor.equals(color)) {
            throw new BadRequestException("Error: wait for your turn");
        }

        try {
            gameData.game().makeMove(command.getMove());
        } catch (InvalidMoveException e) {
            throw new BadRequestException("Error: illegal move");
        }

        gameService.saveGame(gameData);
        connectionManager.messageAll(gameData.gameID(), ServerMessage.loadGame(gameData.game()));

        String message = authData.username() +
                " moved from " +
                convertPosition(command.getMove().getStartPosition()) +
                " to " +
                convertPosition(command.getMove().getEndPosition());
        connectionManager.messageAllButOne(gameData.gameID(), session, ServerMessage.notification(message));
    }

    private String convertPosition(ChessPosition position) {
        char letter = (char) ('a' + (position.getColumn() - 1));
        String row = String.valueOf(position.getRow());
        return letter + row;
    }
}

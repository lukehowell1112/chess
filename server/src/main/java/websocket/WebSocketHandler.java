package websocket;

import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import service.BadRequestException;
import service.GameService;
import service.UnauthorizedException;
import service.UserService;
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
}

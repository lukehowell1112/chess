package websocket;

import service.GameService;
import service.UserService;

public class WebSocketHandler {
    private final UserService userService;
    private final GameService gameService;
    private final ConnectionManager connectionManager;

    public WebSocketHandler(UserService userService, GameService gameService, ConnectionManager connectionManager) {
        this.userService = userService;
        this.gameService = gameService;
        this.connectionManager = connectionManager;
    }

    
}

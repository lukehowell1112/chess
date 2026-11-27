package websocket.messages;

import chess.ChessGame;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    String message;
    String errorMessage;
    ChessGame game;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public static ServerMessage loadGame(ChessGame game) {
        ServerMessage serverMessage = new ServerMessage(ServerMessageType.LOAD_GAME);
        serverMessage.game = game;
        return serverMessage;
    }

    public static ServerMessage error(String errorMessage) {
        ServerMessage serverMessage = new ServerMessage(ServerMessageType.ERROR);
        serverMessage.errorMessage = errorMessage;
        return serverMessage;
    }

    public static ServerMessage notification(String message) {
        ServerMessage serverMessage = new ServerMessage(ServerMessageType.NOTIFICATION);
        serverMessage.message = message;
        return serverMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage that)) {
            return false;
        }
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}

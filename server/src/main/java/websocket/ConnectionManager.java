package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final Map<Integer, Set<Session>> sessionsByGame = new ConcurrentHashMap<>();

    public void add(Integer gameID, Session session) {
        sessionsByGame.computeIfAbsent(gameID, key -> ConcurrentHashMap.newKeySet()).add(session);
    }

    public void remove(Integer gameID, Session session) {
        Set<Session> game = sessionsByGame.get(gameID);
        if (game != null) {
            game.remove(session);
        }
    }

    public void messageOne(Session session, ServerMessage message) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(message);
        session.getRemote().sendString(json);
    }

    public void messageAll(Integer gameID, ServerMessage message) {
        Set<Session> game = sessionsByGame.get(gameID);
        if (game != null) {
            for (Session session : game) {
                try {
                    messageOne(session, message);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void messageAllButOne(Integer gameID, Session rootSession, ServerMessage message) {
        Set<Session> game = sessionsByGame.get(gameID);
        if (game != null) {
            for (Session session : game) {
                if (!rootSession.equals(session)) {
                    try {
                        messageOne(session, message);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}

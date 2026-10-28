package server;

import com.google.gson.Gson;
import dataaccess.*;
import dataaccess.sql.DatabaseSetup;
import io.javalin.*;
import service.*;

import io.javalin.http.Context;
import java.util.Map;

public class Server {
    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();

    private final ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);
    private final UserService userService = new UserService(userDAO, authDAO);
    private final GameService gameService = new GameService(gameDAO, authDAO);

    private final Javalin javalin;

    public Server() {
        try {
            DatabaseSetup.createTables();
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.put("/game", ctx -> {
            String authToken = ctx.header("authorization");
            JoinGameRequest request = new Gson().fromJson(ctx.body(), JoinGameRequest.class);
            gameService.joinGame(authToken, request);
            ctx.result("{}");
        });

        javalin.get("/game", ctx -> {
            String authToken = ctx.header("authorization");
            ListGamesResult result = gameService.listGames(authToken);
            ctx.result(new Gson().toJson(result));
        });

        javalin.post("/game", ctx -> {
            String authToken = ctx.header("authorization");
            CreateGameRequest request = new Gson().fromJson(ctx.body(), CreateGameRequest.class);
            CreateGameResult result = gameService.createGame(authToken, request);
            ctx.result(new Gson().toJson(result));
        });

        javalin.delete("/session", ctx -> {
            String authToken = ctx.header("authorization");
            userService.logout(authToken);
            ctx.result("{}");
        });

        javalin.post("/session", ctx -> {
            LoginRequest request = new Gson().fromJson(ctx.body(), LoginRequest.class);
            LoginResult result = userService.login(request);
            ctx.result(new Gson().toJson(result));
        });

        javalin.post("/user", ctx -> {
            RegisterRequest request = new Gson().fromJson(ctx.body(), RegisterRequest.class);
            RegisterResult result = userService.register(request);
            ctx.result(new Gson().toJson(result));
        });

        javalin.delete("/db", ctx -> {
            clearService.clear();
            ctx.result("{}");
        });

        javalin.exception(BadRequestException.class, (e, ctx) -> sendError(ctx, 400, e));
        javalin.exception(UnauthorizedException.class, (e, ctx) -> sendError(ctx, 401, e));
        javalin.exception(AlreadyTakenException.class, (e, ctx) -> sendError(ctx, 403, e));
        javalin.exception(DataAccessException.class, (e, ctx) -> sendError(ctx, 500, e));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void sendError(Context ctx, int status, Exception e) {
        ctx.status(status);
        ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
    }
}

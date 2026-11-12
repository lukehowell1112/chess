package ui;

import client.GameSummary;
import client.ResponseException;
import client.ServerFacade;

import java.util.List;

public class PostLogin implements InputHandler {
    private final ServerFacade facade;
    private final String authToken;

    public PostLogin(ServerFacade facade, String authToken) {
        this.facade = facade;
        this.authToken = authToken;
    }

    @Override
    public ResultType eval(String input) {
        String[] words = input.split("\\s+");
        if (words.length == 0) {
            return new ResultType("Type help to see commands", false, false);
        }
        String command = words[0].toLowerCase();

        if (command.equals("help")) {
            String menu = """
                    help - to list all commands
                    logout - to simply logout
                    create <game name> - to create a game
                    list - to list the games
                    play <game number> <white|black> - to play a game
                    observe <game number> - to spectate a chosen game""";
            return new ResultType(menu, false, false);
        } else if (command.equals("logout")) {
            if (words.length != 1) {
                return new ResultType("Usage: logout", false, false);
            }
            try {
                facade.logout(authToken);
                return new ResultType("Thanks for playing!", true, false);
            } catch (ResponseException e) {
                return new ResultType("Could not logout: " + e.getMessage(), false, false);
            }
        } else if (command.equals("create")) {
            if (words.length != 2) {
                return new ResultType("Usage: create <game name>", false, false);
            }
            try {
                facade.createGame(authToken, words[1]);
                return new ResultType("Created game: " + words[1], false, false);
            } catch (ResponseException e) {
                return new ResultType("Could not create game: " + e.getMessage(), false, false);
            }
        } else if (command.equals("list")) {
            if (words.length != 1) {
                return new ResultType("Usage: list", false, false);
            }
            try {
                List<GameSummary> gameList = facade.listGames(authToken);
                if (gameList.isEmpty()) {
                    return new ResultType("No games yet", false, false);
                }
                int index = 0;
                StringBuilder text = new StringBuilder();
                for (GameSummary game : gameList) {
                    int gameNumber = index + 1;
                    text.append(gameNumber).append(". ")
                            .append(game.gameName()).append(" ")
                            .append("white: ")
                            .append(userText(game.whiteUsername())).append(", ")
                            .append("black: ")
                            .append(userText(game.blackUsername())).append("\n");
                    index++;
                }
                String result = text.toString();
                return new ResultType(result, false, false);
            } catch (ResponseException e) {
                return new ResultType("Could not list games: " + e.getMessage(), false, false);
            }
        } else if (command.equals("play")) {
            return new ResultType("not yet implemented", false, false);
        } else if (command.equals("observe")) {
            return new ResultType("not yet implemented", false, false);
        }
        return new ResultType("Unknown command, type 'help'", false, false);
    }

    private String userText(String user) {
        return (user == null) ? "open" : user;
    }
}
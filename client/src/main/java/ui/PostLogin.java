package ui;

import chess.ChessBoard;
import client.GameSummary;
import client.ResponseException;
import client.ServerFacade;

import java.util.ArrayList;
import java.util.List;

public class PostLogin implements InputHandler {
    private final ServerFacade facade;
    private final String authToken;
    private List<GameSummary> games = new ArrayList<>();
    private final BoardDrawing drawing = new BoardDrawing();

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
                return new ResultType("Logged out: Thanks for playing!", true, false);
            } catch (ResponseException e) {
                if (e.getStatusCode() == 401) {
                    return new ResultType("Invalid session: please log out and log in again", false, false);
                }
                return new ResultType("Logged out: Thanks for playing!", true, false);
            }
        } else if (command.equals("create")) {
            if (words.length != 2) {
                return new ResultType("Usage: create <game name>", false, false);
            }
            try {
                facade.createGame(authToken, words[1]);
                return new ResultType("Created game: " + words[1], false, false);
            } catch (ResponseException e) {
                if (e.getStatusCode() == 401) {
                    return new ResultType("Invalid session: please log out and log in again", false, false);
                }
                return new ResultType("Could not create game: " + e.getMessage(), false, false);
            }
        } else if (command.equals("list")) {
            if (words.length != 1) {
                return new ResultType("Usage: list", false, false);
            }
            try {
                games = facade.listGames(authToken);
                if (games.isEmpty()) {
                    return new ResultType("No games yet", false, false);
                }
                int index = 0;
                StringBuilder text = new StringBuilder();
                for (GameSummary game : games) {
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
                if (e.getStatusCode() == 401) {
                    return new ResultType("Invalid session: please log out and log in again", false, false);
                }
                return new ResultType("Could not list games: " + e.getMessage(), false, false);
            }
        } else if (command.equals("play")) {
            if (words.length != 3) {
                return new ResultType("Usage: play <game number> <white|black>", false, false);
            }
            int gameNumber;
            try {
                gameNumber = Integer.parseInt(words[1]);
            } catch (NumberFormatException e) {
                return new ResultType("Game number must be an integer", false, false);
            }
            if (games.isEmpty()) {
                return new ResultType("Run list first to see the available games", false, false);
            }
            if (gameNumber > games.size() || gameNumber < 1) {
                return new ResultType("Pick a game number from the list", false, false);
            }
            String color = words[2].toLowerCase();
            if (!color.equals("black") && !color.equals("white")) {
                return new ResultType("Usage: play <game number> <white|black>", false, false);
            }
            try {
                GameSummary game = games.get(gameNumber - 1);
                facade.joinGame(authToken, color.toUpperCase(), game.gameID());
                ChessBoard board = new ChessBoard();
                board.resetBoard();
                return new ResultType("Joined game: " + game.gameName() + "\n" + drawing.drawBoard(board, color.equals("white")), false, false);
            } catch (ResponseException e) {
                if (e.getStatusCode() == 403) {
                    return new ResultType("Unable to join game: that color is taken, please try again", false, false);
                } else if (e.getStatusCode() == 401) {
                    return new ResultType("Invalid session: please log out and log in again", false, false);
                } else {
                    return new ResultType("Unable to join game: " + e.getMessage(), false, false);
                }
            }
        } else if (command.equals("observe")) {
            if (words.length != 2) {
                return new ResultType("Usage: observe <game number>", false, false);
            }
            int gameNumber;
            try {
                gameNumber = Integer.parseInt(words[1]);
            } catch (NumberFormatException e) {
                return new ResultType("Game number must be an integer", false, false);
            }
            if (games.isEmpty()) {
                return new ResultType("Run list first to see the available games", false, false);
            }
            if (gameNumber > games.size() || gameNumber < 1) {
                return new ResultType("Pick a game number from the list", false, false);
            }
            GameSummary game = games.get(gameNumber - 1);
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            return new ResultType("Observing: " + game.gameName() + "\n" + drawing.drawBoard(board, true), false, false);
        }
        return new ResultType("Unknown command, type 'help'", false, false);
    }

    private String userText(String user) {
        return (user == null) ? "open" : user;
    }
}
package ui;

import client.ResponseException;
import client.ServerFacade;

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
                    help
                    logout
                    create
                    list
                    play
                    observe
                    """;
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
            return new ResultType("not yet implemented", false, false);
        } else if (command.equals("list")) {
            return new ResultType("not yet implemented", false, false);
        } else if (command.equals("play")) {
            return new ResultType("not yet implemented", false, false);
        } else if (command.equals("observe")) {
            return new ResultType("not yet implemented", false, false);
        }
        return new ResultType("Unknown command, type 'help'", false, false);
    }
}
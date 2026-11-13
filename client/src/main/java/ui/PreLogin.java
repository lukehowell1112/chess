package ui;

import client.ResponseException;
import client.ServerFacade;
import model.AuthData;

public class PreLogin implements InputHandler {
    private final ServerFacade facade;
    private String authToken;

    public PreLogin(ServerFacade facade) {
        this.facade = facade;
    }

    public String getAuthToken() {
        return authToken;
    }

    @Override
    public ResultType eval(String input) {
        String[] words = input.trim().split("\\s+");
        if (words.length == 0) {
            return new ResultType("Type help to see commands", false, false);
        }
        String command = words[0].toLowerCase();

        if (command.equals("help")) {
            String menu = """
                    register <username> <password> <email> - to create an account
                    login <username> <password> - to log in with an existing account
                    quit - to quit the program
                    help - to list all commands""";
            return new ResultType(menu, false, false);
        } else if (command.equals("quit")) {
            if (words.length != 1) {
                return new ResultType("Usage: quit", false, false);
            }
            return new ResultType("Goodbye!", false, true);
        } else if (command.equals("register")) {
            if (words.length != 4) {
                return new ResultType("Usage: register <username> <password> <email>", false, false);
            }
            try {
                AuthData authData = facade.register(words[1], words[2], words[3]);
                authToken = authData.authToken();
                return new ResultType("Registered and logged in as " + words[1], true, false);
            } catch (ResponseException e) {
                if (e.getStatusCode() == 403) {
                    return new ResultType("Could not register: username is taken, please try again", false, false);
                }
                return new ResultType("Could not register: " + e.getMessage(), false, false);
            }
        } else if (command.equals("login")) {
            if (words.length != 3) {
                return new ResultType("Usage: login <username> <password>", false, false);
            }
            try {
                AuthData authData = facade.login(words[1], words[2]);
                authToken = authData.authToken();
                return new ResultType("Logged in as " + words[1], true, false);
            } catch (ResponseException e) {
                if (e.getStatusCode() == 401) {
                    return new ResultType("Could not log in: username or password is incorrect, please try again", false, false);

                }
                return new ResultType("Could not log in: " + e.getMessage(), false, false);
            }
        }
        return new ResultType("Unknown command, type 'help'", false, false);
    }
}

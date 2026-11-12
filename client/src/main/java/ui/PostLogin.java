package ui;

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
        return new ResultType("admin menu coming soon", false, false);
    }
}

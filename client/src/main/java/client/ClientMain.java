package client;

import chess.*;
import ui.REPL;

public class ClientMain {
    public static void main(String[] args) {
        ServerFacade facade = new ServerFacade(8080);
        REPL repl = new REPL(facade);
        repl.repl();
    }
}

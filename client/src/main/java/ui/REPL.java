package ui;

import client.ServerFacade;

import java.util.Scanner;

public class REPL {
    private final ServerFacade facade;
    private PreLogin preLoginClient;
    private InputHandler current;

    public REPL(ServerFacade facade) {
        this.facade = facade;
        this.preLoginClient = new PreLogin(facade);
        current = preLoginClient;
    }

    public void repl() {
        Scanner scanner = new Scanner(System.in);
        String line = "";
        while (!line.equals("quit")) {
            System.out.print(">");
            line = scanner.nextLine();
            ResultType reply = current.eval(line);
            System.out.println(reply);
            if (reply.switchClient()) {swapClient(reply);}
        }
    }

    private void swapClient(ResultType reply) {
        if (current == preLoginClient) {
            current = new PostLogin(facade, preLoginClient.getAuthToken());
        } else {
            current = preLoginClient;
        }
    }
}


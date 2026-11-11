package ui;

import client.ServerFacade;

import java.util.Scanner;

public class REPL {

    public void repl() {
        InputHandler current
        Scanner scanner = new Scanner(System.in);
        String line = "";
        while (!line.equals("quit")) {
            System.out.print(">");
            line = scanner.nextLine();
            String reply = current.eval(line);
            System.out.println(reply);
        }
    }
}


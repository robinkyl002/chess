package client;

import server.ServerFacade;

import java.util.Scanner;

public class ChessClient {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

    public ChessClient(String url) {
        server = new ServerFacade(url);
    }

    public void run() {
        System.out.println(" Welcome to the pet store. Sign in to start.");
//        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
//            printPrompt();
            String line = scanner.nextLine();

            try {
//                result = eval(line);
//                System.out.print(BLUE + result);
                System.out.println("working on it");
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

}

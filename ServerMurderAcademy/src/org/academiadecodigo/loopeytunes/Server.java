package org.academiadecodigo.loopeytunes;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private Socket playerSocket;
    private LinkedList<ClientConnection> clientConnections;
    private ExecutorService threadPool;
    private BufferedReader in;
    private Game game;
    private int counter = 0;
    private final String LOCK = "lock";

    private Server() {

        try {

            serverSocket = new ServerSocket(9001);
            clientConnections = new LinkedList<>();
            threadPool = Executors.newFixedThreadPool(5);

        } catch (IOException e) {
            System.out.println("Port already in use.");
            System.exit(1);
        }

        awaitingConnections();
    }

    public static void main(String[] args) {
        Server server = new Server();
    }

    private void awaitingConnections() {

        for (int i = 0; i < 4; i++) {

            try {
                String playerName;
                ClientConnection newPlayer;
                playerSocket = serverSocket.accept();

                in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
                if ((playerName = in.readLine()) != null){
                    newPlayer = new ClientConnection(playerSocket, playerName);
                    clientConnections.add(newPlayer);
                    threadPool.submit(newPlayer);
                    System.out.println(newPlayer.name + " is connected");
                }

            } catch (IOException e) {
                System.out.println("Connection lost.");
            }
        }
        //sendStory();
        threadPool.submit(new ConnectionChecker());
        game = new Game();
        clientConnections.get(0).currentPlayer = true;
        sendAll("Game is on!\n");

        next();
    }

    private void sendStory() {
        String[] text = IntroductionText.getText();
        int delay = 4000;

        for (int i=0; i < text.length; i++) {

            try {
                if (text[i].contains("/animation")) {
                    delay = 150;
                    continue;
                }
                if (text[i].contains("/text")) {
                    delay = 4000;
                    continue;
                }
                Thread.sleep(delay);
                sendAll(text[i]);


            } catch (InterruptedException e) {
                System.out.println("Thread interrupted.");
            }

        }
    }

    private void sendAll(String message) {
        synchronized (LOCK) {
            for (ClientConnection cc : clientConnections) {
                cc.send(message);
            }
        }
    }

    private void next() {
        synchronized (LOCK) {
            if (clientConnections.size() == 0) {
                awaitingConnections();
            }
            if (counter >= clientConnections.size()) {
                sendAll(game.getHint());
                counter = 0;
            }
            clientConnections.get(counter).send("It's your turn to guess!");
            clientConnections.get(counter).currentPlayer = false;
            counter++;
            clientConnections.get(counter).currentPlayer = true;
        }
    }

    private void win() {
        sendAll(game.getConfession());
        sendAll("GAME IS OVER\n");

        synchronized (LOCK) {
            for (int i = 0; i < clientConnections.size(); i++) {
                clientConnections.remove(clientConnections.get(i));
            }
        }

        System.out.println("NEW GAME");
        awaitingConnections();
    }

    private class ClientConnection implements Runnable {

        private BufferedReader in;
        private PrintWriter out;
        private Socket playerSocket;
        private final String name;
        private boolean currentPlayer;

        private ClientConnection(Socket playerSocket, String name) {

            this.playerSocket = playerSocket;
            this.name = name;

            try {

                in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
                out = new PrintWriter(new OutputStreamWriter(playerSocket.getOutputStream()));

            } catch (IOException e) {
                System.out.println("Unable to establish a connection to client.");

            }
        }

        public void close() {
            try {
                playerSocket.close();
                in.close();
                out.close();
            } catch (IOException e) {
                System.out.println("Connection lost");
            }

        }

        @Override
        public void run() {
            String guess;
            try {
                while ((guess = in.readLine()) != null) {

                    sendAll(guess);

                    if (game.check(guess)) {
                        sendAll("Detective " + name + " WINS THE GAME\n");
                        win();
                    } else {
                        sendAll("Detective " + name + " failed! Next!\n");
                    }

                    next();
                }

                close();

            } catch (IOException e) {
                System.out.println("Connection lost.");
            }
        }


        private void send(String message) {
            out.println(message);
            out.flush();
        }
    }

    private class ConnectionChecker implements Runnable{

        @Override
        public void run() {
            while (true) {
                synchronized (LOCK) {
                    for (ClientConnection cc : clientConnections) {
                        if (cc.playerSocket.isClosed() && cc.currentPlayer) {
                            clientConnections.remove(cc);
                            next();
                            counter--;
                            continue;
                        }
                        if (cc.playerSocket.isClosed()) {
                            clientConnections.remove(cc);
                        }
                    }
                }
            }
        }
    }
}

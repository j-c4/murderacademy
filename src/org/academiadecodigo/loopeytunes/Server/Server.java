package org.academiadecodigo.loopeytunes.Server;

import org.academiadecodigo.loopeytunes.Lock;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket serverSocket;
    private Socket playerSocket;
    private LinkedList<ClientConnection> clientConnections;
    private ExecutorService threadPool;
    private BufferedReader in;
    private PrintWriter out;
    private Game game;
    private int counter = 0;

    private Server() {

        try {

            serverSocket = new ServerSocket(9001);
            clientConnections = new LinkedList<>();
            threadPool = Executors.newFixedThreadPool(4);
            game = new Game();

        } catch (IOException e) {
            e.printStackTrace();
        }

        awaitingConnections();
    }

    public static void main(String[] args) {

        Server server = new Server();

    }

    private void awaitingConnections() {

        for (int i = 0; i < 4; i++) {

            try {

                playerSocket = serverSocket.accept();

                in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
                out = new PrintWriter(new OutputStreamWriter(playerSocket.getOutputStream()));

                ClientConnection newPlayer = new ClientConnection(playerSocket, in.readLine());
                clientConnections.add(newPlayer);
                threadPool.submit(newPlayer);
                System.out.println(newPlayer.name + " is connected");

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        sendAll("Game is on!");

        next();
    }

    private void sendAll(String message) {
        for (ClientConnection cc : clientConnections) {
            cc.send(message);
        }
    }

    private void next() {
        if(counter > 3){
            counter = 0;
        }
        clientConnections.get(counter).send("It's your turn to guess!");
        counter++;
    }

    private class ClientConnection implements Runnable {

        private BufferedReader in;
        private PrintWriter out;
        private Socket playerSocket;
        private String name;

        private ClientConnection(Socket playerSocket, String name) {

            this.playerSocket = playerSocket;
            this.name = name;

            try {

                in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
                out = new PrintWriter(new OutputStreamWriter(playerSocket.getOutputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            while (!playerSocket.isClosed()) {
                try {

                    String guess = in.readLine();

                    sendAll(guess);

                    sendAll(game.check(guess));

                    next();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        private void send(String message) {
            out.println(message);
            out.flush();
        }
    }
}

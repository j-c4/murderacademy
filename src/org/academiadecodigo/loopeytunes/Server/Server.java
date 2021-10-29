package org.academiadecodigo.loopeytunes.Server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket serverSocket;
    private LinkedList<ClientConnection> list;
    private ExecutorService threads;
    private Game game;
    private int counter;

    public static void main(String[] args) {

        Server server = new Server();

    }

    private Server() {

        try {
            serverSocket = new ServerSocket(9001);
            list = new LinkedList<>();
            threads = Executors.newFixedThreadPool(4);
            game = new Game();
            counter = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }

        start();

    }

    private void start() {

        for (int i = 0; i < 4; i++) {
            try {
                ClientConnection newPlayer = new ClientConnection(serverSocket.accept());
                list.add(newPlayer);
                threads.submit(newPlayer);
                System.out.println(serverSocket.getInetAddress() + " is connected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Game is on");
    }

    private void next() {
        System.out.println("next");

        if (counter == 8) {
            counter =4;
        }

        if (counter > 3) {
            String message = "It is your turn\n";
            list.get(counter-4).send(message);
            counter++;
            return;
        }

        counter++;
    }

    public synchronized void sendAll(String message) {
        for (ClientConnection client : list) {
            client.send(message);
        }
    }

    private class ClientConnection implements Runnable {

        private BufferedReader in;
        private BufferedWriter out;
        private Socket playerSocket;
        private String name;

        private ClientConnection(Socket playerSocket) {
            this.playerSocket = playerSocket;
            try {
                in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            try {
                name = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("hello");
            next();

            while (!playerSocket.isClosed()) {
                receive();
                next();
            }
        }

        private void send(String message) {
            try {
                out.write(message);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void receive() {
            try {
                String message1 = in.readLine();
                String message2 = in.readLine();
                String message3 = in.readLine();
                String message = message1 + " " + message2 + " " + message3;

                sendAll(name + ": " + message);

                if (game.check(message)) {
                    sendAll(name + " won the game!");
                } else {
                    sendAll(name + " failed! Next!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

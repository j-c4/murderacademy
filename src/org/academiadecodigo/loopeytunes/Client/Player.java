package org.academiadecodigo.loopeytunes.Client;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Player {

    private Socket clientSocket;
    private String name;
    private BufferedReader in;
    private Prompt prompt;

    public static void main(String[] args) {

        /*
        if (args.length != 2) {
            System.exit(1);
        }
         */

        Player player = new Player("127.0.0.1", 9001);

        player.playing();

    }

    public Player(String serverIP, int serverPort) {

        try {
            clientSocket = new Socket(serverIP, serverPort);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            prompt = new Prompt(System.in, new PrintStream(clientSocket.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void guess(){

        String[] culpritOptions = {"SID", "PRIS", "VANDO", "PEDRO", "MIGUEL"};
        String[] weaponOptions = {"SKATE", "KEYBOARD", "PUFF", "FIRST MASTER CODER EXCALIBUR", "MEGAPHONE"};
        String[] murderSceneOptions = {"BATHROOM", "GAMINGROOM", "MCROOM", "CLASSROOM", "GRASS"};

        MenuInputScanner culprits = new MenuInputScanner(culpritOptions);
        MenuInputScanner weapons = new MenuInputScanner(weaponOptions);
        MenuInputScanner murderScenes = new MenuInputScanner(murderSceneOptions);

        culprits.setMessage("Who's the culprit?");
        weapons.setMessage("What was the weapon used?");
        murderScenes.setMessage("Where did it happen?");

        int choice1 = prompt.getUserInput(culprits);
        int choice2 = prompt.getUserInput(weapons);
        int choice3 = prompt.getUserInput(murderScenes);

    }

    public void playing() {

        StringInputScanner getName = new StringInputScanner();
        getName.setMessage("What is your name?");
        name = prompt.getUserInput(getName);

        while(!clientSocket.isClosed()) {

            try {
                String message = in.readLine();
                if (message.contains("It is your turn")) {
                    guess();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

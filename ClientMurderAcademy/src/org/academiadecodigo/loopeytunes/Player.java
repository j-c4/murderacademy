package org.academiadecodigo.loopeytunes;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Player {

    private Socket playerSocket;
    private String name;
    private BufferedReader in;
    private PrintWriter out;
    private Prompt terminalPrompt;

    public Player(String serverIP, int serverPort) {

        try {

            playerSocket = new Socket(serverIP, serverPort);

            in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            out = new PrintWriter(playerSocket.getOutputStream());

            terminalPrompt = new Prompt(System.in, System.out);

        } catch (IOException e) {
            System.out.println("Unable to establish a connection to server.");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
/**
        if (!(args.length==1)) {
            System.out.println("YOU NEED TO PUT THE SERVER IP ADDRESS AS ARGUMENT WHEN YOU RUN THE JAR FILE\n");
            return;
        }
*/
        Player player = new Player("127.0.0.1", 9001);

        try {
            player.playing();
        } catch (IOException e) {
            System.out.println("Server was abruptly shut down.");
            System.exit(1);
        }

    }

    public void playing() throws IOException {

        StringInputScanner askName = new StringInputScanner();
        askName.setMessage("What is your name?");
        name = terminalPrompt.getUserInput(askName);

        out.println(name);
        out.flush();
        String message = "";

        while ((message = in.readLine()) != null) {

            System.out.println(message);

            if (message.contains("It's your turn to guess!")) {
                guess();
            }

            if (message.contains("GAME IS OVER")) {
                playerSocket.close();
                in.close();
                out.close();
                System.exit(0);
            }
        }
    }

    public void guess() {
        String[] culpritOptions = {"SID", "PRIS", "VANDO", "PEDRO", "MIGUEL"};
        String[] weaponOptions = {"SKATE", "KEYBOARD", "PUFF", "FIRST MASTER CODER EXCALIBUR", "MEGAPHONE"};
        String[] murderSceneOptions = {"BATHROOM", "GAMINGROOM", "MCROOM", "CLASSROOM", "GRASS"};

        MenuInputScanner culprits = new MenuInputScanner(culpritOptions);
        MenuInputScanner weapons = new MenuInputScanner(weaponOptions);
        MenuInputScanner murderScenes = new MenuInputScanner(murderSceneOptions);

        culprits.setMessage("Who's the culprit?");
        weapons.setMessage("What was the weapon used?");
        murderScenes.setMessage("Where did it happen?");

        int choice1 = terminalPrompt.getUserInput(culprits);
        int choice2 = terminalPrompt.getUserInput(weapons);
        int choice3 = terminalPrompt.getUserInput(murderScenes);

        String playerGuess = "--->" + "Detective " + name + " thinks " + culpritOptions[choice1 - 1] + " killed the code cadet with a " + weaponOptions[choice2 - 1] + " in the " + murderSceneOptions[choice3 - 1];
        out.println(playerGuess);
        out.flush();

    }
}

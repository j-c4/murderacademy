package org.academiadecodigo.loopeytunes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private Culprit culprit;
    private Weapon weapon;
    private MurderScene murderScene;
    private ArrayList<Hints> hintList;
    private int counter = 0;

    //CONSTRUCTOR
    public Game() {
        start();
    }

    //INIT PROPERTIES
    public void start() {

        culprit = Culprit.values()[Random.getRandom()];
        weapon = Weapon.values()[Random.getRandom()];
        murderScene = MurderScene.values()[Random.getRandom()];

        hintList = new ArrayList<>(culprit.getHint().length + weapon.getHint().length + murderScene.getMurderScene().length());
        Collections.addAll(hintList, culprit.hints);
        Collections.addAll(hintList, weapon.hints);
        Collections.addAll(hintList, murderScene.hints);

        System.out.println(culprit.getMotive() + " The weapon used was the " + weapon.getWeapon() + " and it happened in the " + murderScene.getMurderScene() + ".");
    }

    public String getHint() {

        if(hintList.size() <= 0){
            return "Couldn't find anymore clues on the crime scene";
        }

        counter = Random.getRandom(hintList.size());
        Hints hint = hintList.remove(counter);

        return hint.message;
    }

    public String getConfession() {
        return culprit.getMotive();
    }

    // SEE IF THE GUESS OF THE PLAYER IS RIGHT
    public int check(String answer) {
        int guesses = 0;
        if (answer.contains(culprit.getName().toLowerCase())) {
            guesses++;
        }
        if (answer.contains(weapon.getWeapon().toLowerCase())) {
            guesses++;
        }
        if (answer.contains(murderScene.getMurderScene().toLowerCase())) {
            guesses++;
        }
        return guesses;
    }

    //UTILITY CLASSES
    private enum Culprit {
        SID("Sid", new Hints[]{Hints.HINT_C2, Hints.HINT_C3, Hints.HINT_C1}, "he wasn't interacting enough."),
        PRIS("Pris", new Hints[]{Hints.HINT_C4, Hints.HINT_C6}, "he said that she needed to stop saying \"Certo?\"."),
        VANDO("Vando", new Hints[]{Hints.HINT_C5, Hints.HINT_C4, Hints.HINT_C7}, "he broke his skateboard."),
        PEDRO("Pedro", new Hints[]{Hints.HINT_C5, Hints.HINT_C2, Hints.HINT_C3}, "he called him \"puto\"."),
        MIGUEL("Miguel", new Hints[]{ Hints.HINT_C5, Hints.HINT_C4, Hints.HINT_C8}, "he forgot to invite him to the football match.");

        private String name;
        private Hints[] hints;
        private String motive;

        Culprit(String name, Hints[] hints, String motive) {
            this.name = name;
            this.hints = hints;
            this.motive = motive;
        }

        public String getName() {
            return name;

        }

        public Hints[] getHint() {
            return this.hints;
        }

        public String getMotive() {
            return (this == PRIS) ? this.name + " confessed! She murdered the code cadet because " + this.motive : this.name + " confessed! He murdered the code cadet because " + this.motive;
        }

    }

    private enum Weapon {

        SKATEBOARD("skateboard", new Hints[]{Hints.HINT_W5, Hints.HINT_W6}),
        KEYBOARD("keyboard", new Hints[]{Hints.HINT_W1, Hints.HINT_W2, Hints.HINT_W7}),
        PUFF("puff", new Hints[]{Hints.HINT_W4}),
        SWORD("First Master Coder EXCALIBUR", new Hints[]{Hints.HINT_W2}),
        MEGAPHONE("Megaphone", new Hints[]{Hints.HINT_W1, Hints.HINT_W2, Hints.HINT_W3});

        private String weapon;
        private Hints[] hints;

        Weapon(String weapon, Hints[] hints) {
            this.weapon = weapon;
            this.hints = hints;

        }

        public String getWeapon() {
            return weapon;

        }

        public Hints[] getHint() {
            return this.hints;
        }

    }

    private enum MurderScene {

        BATHROOM("bathroom", new Hints[]{Hints.HINT_M1}),
        GAMINGROOM("gaming room", new Hints[]{Hints.HINT_M1, Hints.HINT_M2}),
        MCROOM("Master Coders' room", new Hints[]{Hints.HINT_M3}),
        CLASSROOM("classroom", new Hints[]{Hints.HINT_M2, Hints.HINT_M3}),
        GRASS("grass", new Hints[]{Hints.HINT_M1, Hints.HINT_M2});

        private String murderScene;
        private Hints[] hints;

        MurderScene(String murderScene, Hints[] hints) {
            this.murderScene = murderScene;
            this.hints = hints;

        }

        public String getMurderScene() {

            return murderScene;

        }

    }

    private enum Hints {
        HINT_C1("Clue: A long strand of light colored hair was found on the ground.\n"),
        HINT_C2("Clue: DNA tests revealed that the culprit might be a tall man.\n"),
        HINT_C3("Clue: A beard hair was found on the ground.\n"),
        HINT_C4("Clue: The security guard saw an average height person leaving the campus.\n"),
        HINT_C5("Clue: The security guard saw someone leaving with an Apple laptop covered with stickers.\n"),
        HINT_C6("Clue: A photo of a cat was found near the body!\n"),
        HINT_C7("Clue: The victim was found grabbing a t-shirt with an anime motif.\n"),
        HINT_C8("Clue: A small baby's toy was found under the victim's body"),
        HINT_M1("Clue: The body's clothes smell really bad.\n"),
        HINT_M2("Clue: There was black paint on the victims fingernails.\n"),
        HINT_M3("Clue: The body was found grabbing a chair.\n"),
        HINT_W1("Clue: There were some mechanical components on the victim's body.\n"),
        HINT_W2("Clue: The security guard saw someone leaving with a small backpack. Maybe the weapon is small...\n"),
        HINT_W3("Clue: The coders from the office next door heard a loud and unrecognizable noise.\n"),
        HINT_W4("Clue: The body shows signs of asphyxiation.\n"),
        HINT_W5("Clue: There was wood chips on the victim's body.\n"),
        HINT_W6("Clue: One item was missing at the academy...something that kept cadets rolling...\n"),
        HINT_W7("Clue: The marks on the body are resembling a net of small squares\n");

        private String message;

        Hints(String message) {
            this.message = message;
        }
    }

    private final static class Random {

        public static int getRandom() {
            return (int) Math.floor(Math.random() * 5);
        }

        public static int getRandom(int n) {
            return (int) Math.floor(Math.random() * n);
        }
    }

}

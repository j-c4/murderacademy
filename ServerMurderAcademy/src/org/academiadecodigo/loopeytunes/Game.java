package org.academiadecodigo.loopeytunes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private Culprit culprit;
    private Weapon weapon;
    private MurderScene murderScene;
    private List<Hints> hintList;
    private int counter;

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

        counter = Random.getRandom(0, hintList.size());
        Hints hint = hintList.get(counter);
        String hintText = hintList.get(counter).message;

        while (hint.used) {
            counter = Random.getRandom();
            hint = hintList.get(counter);
            hintText = hintList.get(counter).message;
        }

        hint.used = true;
        return hintText;
    }

    public String getConfession() {
        return culprit.getMotive();
    }

    // SEE IF THE GUESS OF THE PLAYER IS RIGHT
    public boolean check(String answer) {
        return (answer.contains(culprit.getName().toLowerCase()) && answer.contains(weapon.getWeapon().toLowerCase()) && answer.contains(murderScene.getMurderScene().toLowerCase()));
    }

    //UTILITY CLASSES
    private enum Culprit {
        SID("Sid", new Hints[]{Hints.HINT_C1, Hints.HINT_C2, Hints.HINT_C3}, "he wasn't interacting enough."),
        PRIS("Pris", new Hints[]{Hints.HINT_C1, Hints.HINT_C4, Hints.HINT_C5}, "he said that she needed to stop saying \"Certo?\"."),
        VANDO("Vando", new Hints[]{Hints.HINT_C2, Hints.HINT_C4, Hints.HINT_C5}, "he broke his skateboard."),
        PEDRO("Pedro", new Hints[]{Hints.HINT_C1, Hints.HINT_C2, Hints.HINT_C3, Hints.HINT_C5}, "he called him \"puto\"."),
        MIGUEL("Miguel", new Hints[]{Hints.HINT_C2, Hints.HINT_C3, Hints.HINT_C4, Hints.HINT_C5}, "he forgot to invite him to the football match.");

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

        SKATEBOARD("skateboard", new Hints[]{Hints.HINT_W1, Hints.HINT_W3}),
        KEYBOARD("keyboard", new Hints[]{Hints.HINT_W1, Hints.HINT_W2, Hints.HINT_W3}),
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

        HINT_C1("Hint: A long strand of hair was found on the ground.\n"),
        HINT_C2("Hint: DNA tests revealed that the culprit might be a man.\n"),
        HINT_C3("Hint: A beard hair was found on the ground.\n"),
        HINT_C4("Hint: The security guard saw an average height person leaving the campus.\n"),
        HINT_C5("Hint: The security guard saw someone leaving with an Apple laptop covered with stickers.\n"),
        HINT_M1("The body's clothes smell really bad.\n"),
        HINT_M2("There was black paint on the victims fingernails.\n"),
        HINT_M3("The body was found grabbing a chair.\n"),
        HINT_W1("There were some mechanical components on the victim's body.\n"),
        HINT_W2("The security guard saw someone leaving with a small backpack.\n"),
        HINT_W3("The coders from the office next door heard a loud and unrecognizable noise.\n"),
        HINT_W4("The body shows signs of asphyxiation.\n");

        private String message;
        private boolean used;

        Hints(String message) {
            this.message = message;
            this.used = false;
        }
    }

    private final static class Random {

        public static int getRandom() {
            return (int) Math.floor(Math.random() * 5);
        }

        public static int getRandom(int max, int min) {
            return (int) Math.floor(Math.random() * (max - min) + min);
        }
    }

}

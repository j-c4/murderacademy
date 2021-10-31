package org.academiadecodigo.loopeytunes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private Culprit culprit;
    private Weapon weapon;
    private MurderScene murderScene;
    private List<Hints> hintList;
    private int counter = 0;

    //CONSTRUCTOR
    public Game() {
        start();
    }

    //INIT PROPERTIES
    public void start() {

        culprit = Culprit.values()[(int) Math.floor(Math.random() * 5)];
        weapon = Weapon.values()[(int) Math.floor(Math.random() * 5)];
        murderScene = MurderScene.values()[(int) Math.floor(Math.random() * 5)];

        hintList = new ArrayList<>(culprit.getHint().length + weapon.getHint().length + murderScene.getMurderScene().length());
        Collections.addAll(hintList, culprit.hints);
        Collections.addAll(hintList, weapon.hints);
        Collections.addAll(hintList, murderScene.hints);

        System.out.println(culprit.getMotive() + " The weapon used was the " + weapon.getWeapon() + " and it happened in the " + murderScene.getMurderScene() + ".");
    }

    public String getHint() {
        if (counter == hintList.size()) {
            counter = 0;
        }
        String hint = hintList.get(counter).getMessage();
        counter++;
        return hint;
    }

    public String getConfession() {
        return culprit.getMotive();
    }

    // SEE IF THE GUESS OF THE PLAYER IS RIGHT
    public boolean check(String answer) {
        return (answer.contains(culprit.getName()) && answer.contains(weapon.getWeapon()) && answer.contains(murderScene.getMurderScene()));
    }

    //UTILITY CLASSES
    private enum Culprit {
        SID("SID", new Hints[]{Hints.HINT_C1, Hints.HINT_C2, Hints.HINT_C3}, "he wasn't interacting enough."),
        PRIS("PRIS", new Hints[]{Hints.HINT_C1, Hints.HINT_C4, Hints.HINT_C5}, "he said that she needed to stop saying \"right\"."),
        VANDO("VANDO", new Hints[]{Hints.HINT_C3, Hints.HINT_C4, Hints.HINT_C5}, "he broke his skateboard."),
        PEDRO("PEDRO", new Hints[]{Hints.HINT_C1, Hints.HINT_C2, Hints.HINT_C3, Hints.HINT_C5}, "he called him \"puto\"."),
        MIGUEL("MIGUEL", new Hints[]{Hints.HINT_C2, Hints.HINT_C3, Hints.HINT_C4, Hints.HINT_C5}, "he forgot to invite him to the football match.");

        private String name;
        private Hints[] hints;
        private String motive;

        Culprit(String name, Hints[] hints, String motive){
            this.name = name;
            this.hints = hints;
            this.motive = motive;
        }

        public String getName(){
            return name;

        }

        public Hints[] getHint() {
            return this.hints;
        }

        public String getMotive() {
                return (this == PRIS) ? this.name + " confessed! She murdered the code cadet because " + this.motive : this.name + " confesed! He murdered the code cadet because " + this.motive;
        }

    }
    private enum Weapon {

        SKATEBOARD("SKATEBOARD", new Hints[]{Hints.HINT_W1, Hints.HINT_W3}),
        KEYBOARD("KEYBOARD", new Hints[]{Hints.HINT_W1, Hints.HINT_W2, Hints.HINT_W3}),
        PUFF("PUFF", new Hints[]{Hints.HINT_W4}),
        SWORD("FIRST MASTER CODER EXCALIBUR", new Hints[]{Hints.HINT_W2}),
        MEGAPHONE("MEGAPHONE", new Hints[]{Hints.HINT_W1, Hints.HINT_W2, Hints.HINT_W3});

        private String weapon;
        private Hints[] hints;

        Weapon(String weapon, Hints[] hints){
            this.weapon = weapon;
            this.hints = hints;

        }

        public String getWeapon(){
            return weapon;

        }

        public Hints[] getHint() {
            return this.hints;
        }

    }
    private enum MurderScene {

        BATHROOM("BATHROOM", new Hints[]{Hints.HINT_M1}),
        GAMINGROOM("GAMINGROOM", new Hints[]{Hints.HINT_M1, Hints.HINT_M2}),
        MCROOM("MCROOM", new Hints[]{Hints.HINT_M3}),
        CLASSROOM("CLASSROOM", new Hints[]{Hints.HINT_M2, Hints.HINT_M3}),
        GRASS("GRASS", new Hints[]{Hints.HINT_M1, Hints.HINT_M2});

        private String murderScene;
        private Hints[] hints;

        MurderScene(String murderScene, Hints[] hints){
            this.murderScene = murderScene;
            this.hints = hints;

        }

        public String getMurderScene(){

            return murderScene;

        }

        public Hints[] getHint() {
            return this.hints;
        }
    }
    private enum Hints {
        HINT_C1("A long string of hair was found on the ground."),
        HINT_C2("DNA tests revealed that the culprit might be a man."),
        HINT_C3("A beard hair was found on the ground."),
        HINT_C4("The security guard saw an average height person leaving the campus."),
        HINT_C5("The security guard saw someone leaving with an Apple laptop covered with stickers."),
        HINT_M1("The body's clothes smell really bad."),
        HINT_M2("There was black paint on the victims fingernails."),
        HINT_M3("The body was found grabbing a chair."),
        HINT_W1("There were some mechanical components on the victim's body."),
        HINT_W2("The security guard saw someone leaving with a small backpack."),
        HINT_W3("The coders from the office next door heard a loud and unrecognizable noise."),
        HINT_W4("The body shows signs of asphyxiation.");

        private String message;

        Hints(String message) {
            this.message = message;
        }

        private String getMessage() {
            return message;
        }

    }
}

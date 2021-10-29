package org.academiadecodigo.loopeytunes.Server;

public class Game {
    private String culprit;
    private String weapon;
    private String murderScene;

    public Game() {
        start();
    }

    public void start() {

        culprit = Culprit.values()[(int) Math.floor(Math.random() * 5)].getName();
        weapon = Weapon.values()[(int) Math.floor(Math.random() * 5)].getWeapon();
        murderScene = MurderScene.values()[(int) Math.floor(Math.random() * 5)].getMurderScene();

        System.out.println(culprit + " " + weapon + " " + murderScene);
    }

    public boolean check(String answer) {

        return (answer.contains(culprit) && answer.contains(weapon) && answer.contains(murderScene));
    }

    private enum Culprit {
        SID("SID"),
        PRIS("PRIS"),
        VANDO("VANDO"),
        PEDRO("PEDRO"),
        MIGUEL("MIGUEL");

        private String name;

        Culprit(String name){
            this.name = name;
        }

        public String getName(){

            return name;

        }

    }

    private enum Weapon {

        SKATE("SKATE"),
        KEYBOARD("KEYBOARD"),
        PUFF("PUFF"),
        SWORD("FIRST MASTER CODER EXCALIBUR"),
        MEGAPHONE("MEGAPHONE");

        private String weapon;

        Weapon(String weapon){
            this.weapon = weapon;
        }

        public String getWeapon(){

            return weapon;

        }

    }

    private enum MurderScene {

        BATHROOM("BATHROOM"),
        GAMINGROOM("GAMINGROOM"),
        MCROOM("MCROOM"),
        CLASSROOM("CLASSROOM"),
        GRASS("GRASS");

        private String murderScene;

        MurderScene(String murderScene){
            this.murderScene = murderScene;
        }

        public String getMurderScene(){

            return murderScene;

        }
    }
}

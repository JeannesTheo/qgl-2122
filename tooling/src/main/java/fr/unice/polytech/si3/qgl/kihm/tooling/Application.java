package fr.unice.polytech.si3.qgl.kihm.tooling;

import fr.unice.polytech.si3.qgl.kihm.simulator.Simulator;
import fr.unice.polytech.si3.qgl.kihm.structures.Parser;

public class Application {

    public static void main(String[] args) {
//        Cockpit c = new Cockpit();
//        c.initGame(Parser.fileToString("tooling/src/main/java/fr/unice/polytech/si3/qgl/kihm/tooling/exemplesjson/basic/initGame.json"));
//        String s = c.nextRound(Parser.fileToString("tooling/src/main/java/fr/unice/polytech/si3/qgl/kihm/tooling/exemplesjson/basic/nextRound.json"));
//        System.out.println(s);
        Simulator.get().run(Parser.fileToString("tooling/src/main/resources/Games/week6/initGame.json"));
    }
}

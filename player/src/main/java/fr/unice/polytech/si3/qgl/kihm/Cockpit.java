package fr.unice.polytech.si3.qgl.kihm;

import fr.unice.polytech.si3.qgl.kihm.logger.Printer;
import fr.unice.polytech.si3.qgl.kihm.structures.Game;
import fr.unice.polytech.si3.qgl.kihm.structures.Parser;
import fr.unice.polytech.si3.qgl.regatta.cockpit.ICockpit;

import java.util.ArrayList;
import java.util.List;

public class Cockpit implements ICockpit {
    private final Parser parser;
    private Game game;
    private int numTour = 0;

    public Cockpit() {
        parser = new Parser();
    }

    public void initGame(String gameString) {
        Printer.get().info("Init game input: " + gameString.replace(" ", "").replace(",", ", "));
        game = parser.readInitGame(gameString);
    }

    public String nextRound(String round) {
        if (game != null && game.getShip() != null) {
            Printer.get().finest("");
            Printer.get().info(" === Tour " + ++this.numTour + " ===");
            Printer.get().info("Next round input: " + round.replace(" ", "").replace(",", ", "));
            game = parser.readNextRound(round, game);
            String actions = parser.constructJson(game.play());
            Printer.get().info("Actions: " + actions);
            return actions;
        }
        return "[]";
    }

    @Override
    public List<String> getLogs() {
        return new ArrayList<>();
    }

    public Game getGame() {
        return game;
    }
}

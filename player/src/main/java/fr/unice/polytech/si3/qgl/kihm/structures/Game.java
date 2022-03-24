package fr.unice.polytech.si3.qgl.kihm.structures;

import fr.unice.polytech.si3.qgl.kihm.actions.Action;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle;
import fr.unice.polytech.si3.qgl.kihm.landmarks.Wind;
import fr.unice.polytech.si3.qgl.kihm.landmarks.World;
import fr.unice.polytech.si3.qgl.kihm.logger.Printer;
import fr.unice.polytech.si3.qgl.kihm.ship.Sailor;
import fr.unice.polytech.si3.qgl.kihm.ship.SailorManager;
import fr.unice.polytech.si3.qgl.kihm.ship.Ship;
import fr.unice.polytech.si3.qgl.kihm.simulator.Simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.isNull;

/**
 * Class with all information of the game
 */
public class Game {
    private String goalName;
    private World world;
    private int shipCount;
    private Ship ship;
    private List<Sailor> sailors;
    private List<Obstacle> seaEntities;
    private Pathfinder pathfinder;
    private SailorManager sailorManager;

    public void init() {
        pathfinder = new Pathfinder(ship);
        sailorManager = new SailorManager(sailors, ship);
    }

    /**
     * Main method of the game
     * Decide the game plan
     *
     * @return the list of actions needed
     */
    public List<Action> play() {
        if (this.checkpointReached()) {
            Printer.get().fine("***Checkpoint was Reached***");
            this.world.getCheckpoints().remove(0);
        }
        List<Action> actions = new ArrayList<>();
        if (!isNull(ship) && !world.getCheckpoints().isEmpty()) {
            Map<String, Double> layout = this.pathfinder.nextRoundLayout(this.sailors.size());
            actions.addAll(sailorManager.moveSailors(layout));
            actions.addAll(sailorManager.whoUseEquipments(layout));
        }
        return actions;
    }

    private boolean checkpointReached() {
        return Simulator.get().collision(world.getCheckpoints().get(0).getShape(), this.ship.getShape());
    }

    public int getShipCount() {
        return shipCount;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public List<Sailor> getSailors() {
        return sailors;
    }

    public void setSailors(List<Sailor> sailors) {
        this.sailors = sailors;
    }

    public List<Obstacle> getSeaEntities() {
        return seaEntities;
    }

    public void setSeaEntities(List<Obstacle> seaEntities) {
        this.seaEntities = seaEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return shipCount == game.shipCount && Objects.equals(goalName, game.goalName) && Objects.equals(ship, game.ship) && Objects.equals(sailors, game.sailors) && Objects.equals(pathfinder, game.pathfinder) && Objects.equals(sailorManager, game.sailorManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goalName, shipCount, ship, sailors, pathfinder, sailorManager);
    }

    public Pathfinder getPathfinder() {
        return pathfinder;
    }

    public void setPathfinder(Pathfinder pathfinder) {
        this.pathfinder = pathfinder;
    }

    public World getWorld() {
        return world;
    }


    public void setWorld(World world) {
        this.world = world;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    @Override
    public String toString() {
        return "Game{" +
                "goal=" + goalName +
                ", shipCount=" + shipCount +
                ", ship=" + ship +
                ", sailors=" + sailors +
                ", pathfinder=" + pathfinder +
                ", sailorManager=" + sailorManager +
                '}';
    }

    public SailorManager getSailorManager() {
        return sailorManager;
    }

    public void setSailorManager(SailorManager sailorManager) {
        this.sailorManager = sailorManager;
    }

    public void update(Wind w, List<Obstacle> entities) {
        this.world.setEntities(entities);
        this.world.setWind(w);
        this.pathfinder = new Pathfinder(this.ship, this.world);
    }
}

package fr.unice.polytech.si3.qgl.kihm.structures;

import fr.unice.polytech.si3.qgl.kihm.equipment.Sail;
import fr.unice.polytech.si3.qgl.kihm.landmarks.CheckPoint;
import fr.unice.polytech.si3.qgl.kihm.landmarks.World;
import fr.unice.polytech.si3.qgl.kihm.logger.Printer;
import fr.unice.polytech.si3.qgl.kihm.ship.Ship;
import fr.unice.polytech.si3.qgl.kihm.simulator.Simulator;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static fr.unice.polytech.si3.qgl.kihm.equipment.Equipment.*;
import static java.lang.Math.PI;
import static java.lang.Math.round;
import static java.util.Objects.hash;

public class Pathfinder {
    private final Ship ship;
    private final World world;

    public Pathfinder(Ship ship) {
        this.ship = ship;
        this.world = new World();
    }

    public Pathfinder(Ship ship, World world) {
        this.ship = ship;
        this.world = world;
    }

    public Map<String, Double> nextRoundLayout(int numberOfSailors) {
        CheckPoint nextCheckpoint = this.world.getCheckpoints().get(0);
        Map<String, Double> layout = this.getLayout(this.calculAngle(nextCheckpoint), numberOfSailors);

        // Methode when there is more than 1 checkpoint remaining
        if (world.getCheckpoints().size() > 1) {
            CheckPoint preShotCheckpoint = this.world.getCheckpoints().get(1);
            Map<String, Double> preShotLayout = this.getLayout(this.calculAngle(preShotCheckpoint), numberOfSailors);

            // Checks if pre-shooting the checkpoint we still get the next checkpoint
            if (nextCheckpoint.distance(this.nextRoundPosition(preShotLayout)) <= nextCheckpoint.getShape().getBounds().getHeight() * 0.5) {
                layout = preShotLayout;
            }
        }

        // Slows the ship down if needed to get the checkpoint
        if (numberOfSailors > 0) this.slowDown(layout, nextCheckpoint, this.calculAngle(nextCheckpoint));
        return layout;
    }

    /**
     * Compute number of oar on each side and the angle for the rudder, in order to move the boat with the better angle
     *
     * @return [Number of left oar needed, Number of right oar needed, Angle of the rudder]
     */
    public Map<String, Double> getLayout(double angle, int numberOfSailors) {
        Map<String, Double> layout = new HashMap<>();

        double angleRudder = 0.0;
        double nbLeft = ship.getOarLeft().size();
        double nbRight = ship.getOarRight().size();
        double delta = PI / (nbRight + nbLeft);
        double nbSail = 0.0;
        boolean hasRudder = this.ship.hasEquipment(equipmentTypeEnum.RUDDER) && numberOfSailors > 2;

        if ((!hasRudder && Math.abs(angle) < delta) || (hasRudder && Math.abs(angle) <= PI / 4)) {
            if (hasRudder && (numberOfSailors >= this.ship.getEquipments().size() || Math.abs(angle) >= Math.toRadians(2))) {
                angleRudder = angle;
            }
            nbRight = nbLeft = Math.min(ship.getOarLeft().size(), ship.getOarRight().size());
        } else {
            int diffRL = (int) (angle / delta);
            if (hasRudder) {
                double alpha = angle - PI / 4;
                diffRL = (int) (alpha / delta) + 1;
            }
            if (diffRL > 0) {
                while (nbLeft < nbRight - diffRL) {
                    nbRight--;
                }
                nbLeft = nbRight - diffRL;
            } else {
                while (nbRight < nbLeft + diffRL) {
                    nbLeft--;
                }
                nbRight = nbLeft + diffRL;
            }
            nbLeft = Math.max(nbLeft, 0);
            nbRight = Math.max(nbRight, 0);
            if (hasRudder) {
                double oarAngle = (nbRight - nbLeft) * delta;
                angleRudder = angle - oarAngle;
            }
        }

        //si le bateau et le vent sont favorables au debut et a la fin du tour
        if (ship.hasEquipment(equipmentTypeEnum.SAIL) && this.world.getWind().getStrength() > 0 && isWindFavorable(this.world.getWind().getOrientation(), ship.getPosition().getOrientation()) && isWindFavorable(this.world.getWind().getOrientation(), this.ship.getPosition().getOrientation() + ((nbRight - nbLeft) * delta + angleRudder))) {
            nbSail = ship.getSails().size();
        }

        layout.put(OAR_LEFT, nbLeft);
        layout.put(OAR_RIGHT, nbRight);
        layout.put(RUDDER, angleRudder);
        layout.put(SAIL, nbSail);

        while (this.calculateNumberOfSailorsNeeded(layout) > numberOfSailors && (layout.get(OAR_LEFT) > 0 || layout.get(OAR_RIGHT) > 0)) {
            layout.put(OAR_LEFT, Math.max(layout.get(OAR_LEFT) - 1, 0));
            layout.put(OAR_RIGHT, Math.max(layout.get(OAR_RIGHT) - 1, 0));
        }
        Printer.get().fine("Objectif " + String.format("%.2f", Math.toDegrees(angle)) + "° -> Turned " + String.format("%.2f", Math.toDegrees((layout.get(OAR_RIGHT) - layout.get(OAR_LEFT)) * delta + layout.get(RUDDER))));
        return layout;
    }

    private int calculateNumberOfSailorsNeeded(Map<String, Double> layout) {
        int assignedToSails = (int) Math.abs(layout.get(SAIL).intValue() - this.ship.getSails().stream().filter(Sail::isOpened).count());
        return layout.get(OAR_LEFT).intValue() + layout.get(OAR_RIGHT).intValue() + (layout.get(RUDDER) != 0.0 ? 1 : 0) + assignedToSails;
    }

    private void slowDown(Map<String, Double> layout, CheckPoint checkPoint, double angle) {
        boolean early;
        // Decides when it gets to the checkpoint if it's better to stop early in the checkpoint or not
        if (this.world.getCheckpoints().size() > 1) {
            early = Math.toDegrees(this.angleBetweenTwoCheckpoints(this.world.getCheckpoints().get(0), this.world.getCheckpoints().get(1))) <= 100;
        } else {
            early = false;
        }

        double radius = checkPoint.getShape().getBounds().height * 0.5;
        // Slow down to not miss the checkpoint
        while (layout.get(OAR_LEFT) >= 0 && layout.get(OAR_RIGHT) >= 0 && checkPoint.distance(this.ship.getPosition()) + radius < this.travelDistance(layout) && checkPoint.distance(this.nextRoundPosition(layout)) > radius) {
            layout.put(OAR_LEFT, layout.get(OAR_LEFT) - 1);
            layout.put(OAR_RIGHT, layout.get(OAR_RIGHT) - 1);
        }

        // Slow down to stop earlier in the checkpoint
        while (early && layout.get(OAR_LEFT) >= 0 && layout.get(OAR_RIGHT) >= 0 && checkPoint.distance(this.ship.getPosition()) - radius / 2 < this.travelDistance(layout) && checkPoint.distance(this.nextRoundPosition(layout)) <= radius) {
            layout.put(OAR_LEFT, layout.get(OAR_LEFT) - 1);
            layout.put(OAR_RIGHT, layout.get(OAR_RIGHT) - 1);
        }
        if (layout.get(OAR_LEFT) <= 0 && layout.get(OAR_RIGHT) <= 0) {
            if (angle < 0) {
                layout.put(OAR_LEFT, 1.0);
                layout.put(OAR_RIGHT, 0.0);
            }
            if (angle > 0) {
                layout.put(OAR_LEFT, 0.0);
                layout.put(OAR_RIGHT, 1.0);
            }
        }
    }

    public Position nextRoundPosition(Map<String, Double> layout) {
        Position shipPos = this.ship.getPosition();
        Position nextRound = new Position(shipPos.getX(), shipPos.getY(), shipPos.getOrientation());
        int totalOars = this.ship.getOarLeft().size() + this.ship.getOarRight().size();
        double oarSpeed = Simulator.get().calculateOarSpeed(layout, totalOars);
        double angle = Simulator.get().calculateTurningAngle(layout, totalOars);
        double windStrength = Simulator.get().calculateWindStrength(this.world.getWind(), this.ship.getSails());
        return Simulator.get().calculatingNextRoundPosition(nextRound, this.ship.getShape(), oarSpeed, angle, windStrength, this.world.getWind().getOrientation(), this.world.getStreams());
    }

    private double travelDistance(Map<String, Double> layout) {
        Position before = this.ship.getPosition();
        Position after = this.nextRoundPosition(layout);
        return after.distance(before);
    }

    double calculAngle(CheckPoint c) {
        double angle;
        boolean hasRudder = this.ship.hasEquipment(equipmentTypeEnum.RUDDER);
        if (c.distance(this.ship.getPosition()) > c.getShape().getBounds().getHeight() * 1 && hasRudder) {
            angle = this.ship.getPosition().getAngleBetween(getClosestPoint(this.ship.getPosition(), c.getShape()));
        } else {
            angle = this.ship.getPosition().getAngleBetween(c.getPosition());
        }
        return angle;
    }

    double angleBetweenTwoCheckpoints(CheckPoint cp1, CheckPoint cp2) {
        // arccos[(a² + c² − b²) ÷ 2ac]
        double a = cp1.distance(cp2.getPosition());
        double b = cp2.distance(this.ship.getPosition());
        double c = cp1.distance(this.ship.getPosition());
        return Math.acos((Math.pow(a, 2) + Math.pow(c, 2) - Math.pow(b, 2)) / (2 * a * c));
    }

    private boolean isWindFavorable(double windOrientation, double shipOrientation) {
        return Math.cos(shipOrientation - windOrientation) > 0;
    }

    protected Position getClosestPoint(Position position, Shape shape) {
        Position closestPoint = new Position();
        Rectangle2D bounds = shape.getBounds2D();
        double distanceCarre = Integer.MAX_VALUE;
        double radius = bounds.getHeight() * 0.5;
        double centerX = bounds.getX() + radius;
        double centerY = bounds.getY() + radius;
        for (double y = centerY - radius; y <= centerY + radius; y += 0.1) {

            double dx = Math.pow(radius, 2) - Math.pow(y - centerY, 2);
            boolean negative = dx < 0.0;
            dx = Math.sqrt(Math.abs(dx));
            if (negative) dx *= -1;
            dx = radius - dx;

            if (distanceCarre > (Math.pow(centerX - radius + dx - position.getX(), 2) + Math.pow(y - position.getY(), 2))) {
                distanceCarre = Math.pow(centerX - radius + dx - position.getX(), 2) + Math.pow(y - position.getY(), 2);
                closestPoint.setX(centerX - radius + dx);
                closestPoint.setY(y);
            }

            if (distanceCarre > (Math.pow(centerX + radius - dx - position.getX(), 2) + Math.pow(y - position.getY(), 2))) {
                distanceCarre = Math.pow(centerX + radius - dx - position.getX(), 2) + Math.pow(y - position.getY(), 2);
                closestPoint.setX(centerX + radius - dx);
                closestPoint.setY(y);
            }
        }
        closestPoint.setX((round(closestPoint.getX() * 100)) / 100.0);
        closestPoint.setY((round(closestPoint.getY() * 100)) / 100.0);
        return closestPoint;
    }

    @Override
    public String toString() {
        return "Pathfinder{" + "ship=" + ship + ", world=" + world + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pathfinder that = (Pathfinder) o;
        return Objects.equals(ship, that.ship) && Objects.equals(world, that.world);
    }

    @Override
    public int hashCode() {
        return hash(ship, world);
    }
}

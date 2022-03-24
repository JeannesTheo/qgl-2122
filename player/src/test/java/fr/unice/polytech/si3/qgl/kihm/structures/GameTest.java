package fr.unice.polytech.si3.qgl.kihm.structures;

import fr.unice.polytech.si3.qgl.kihm.equipment.Equipment;
import fr.unice.polytech.si3.qgl.kihm.equipment.Oar;
import fr.unice.polytech.si3.qgl.kihm.equipment.Rudder;
import fr.unice.polytech.si3.qgl.kihm.ship.Deck;
import fr.unice.polytech.si3.qgl.kihm.ship.Ship;
import fr.unice.polytech.si3.qgl.kihm.simulator.Simulator;
import fr.unice.polytech.si3.qgl.kihm.utilities.PointDouble;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    final ArrayList<Equipment> equipment = new ArrayList<>();
    Game g;
    Ship sh;
    Equipment oar1;
    Equipment oar2;
    Equipment rudder;
    Map<String, Double> layout;

    @BeforeEach
    void setUp() {
        this.g = new Game();
        this.layout = new HashMap<>();
        this.oar1 = new Oar(0, 0);
        this.oar2 = new Oar(0, 1);
        this.rudder = new Rudder(0, 1);
        this.equipment.add(oar1);
        this.equipment.add(oar2);
        sh = new Ship("Le Royal Kihm", 500, new Position(0, 0), new Rectangle(2, 3), new Deck(2, 3), equipment);
        g.setShip(sh);
    }

    @Test
    void collisionCheckpointAndShipNoContact() {
        double rayon = 5;
        PointDouble centerCircle = new PointDouble(-5, 5);
        Shape checkPointShape = new Ellipse2D.Double(centerCircle.getX() - rayon, centerCircle.getY() - rayon, 2 * rayon, 2 * rayon);
        Shape shipShape = new Polygon(new int[]{-2, 0, 12, 14, 8}, new int[]{0, 2, 8, 2, -2}, 5);
        assertFalse(Simulator.get().collision(shipShape, checkPointShape));
    }

    @Test
    void collisionCheckpointAndShipPointContact() {
        double rayon = 5;
        PointDouble centerCircle = new PointDouble(-5, 5);
        Shape checkPointShape = new Ellipse2D.Double(centerCircle.getX() - rayon, centerCircle.getY() - rayon, 2 * rayon, 2 * rayon);
        Shape shipShape = new Polygon(new int[]{-2, -1, 12, 14, 8}, new int[]{0, 2, 8, 2, -2}, 5);
        assertTrue(Simulator.get().collision(shipShape, checkPointShape));
    }

    @Test
    void collisionCheckpointAndShipLineContact() {
        double rayon = 5;
        PointDouble centerCircle = new PointDouble(-5, 5);
        Shape checkPointShape = new Ellipse2D.Double(centerCircle.getX() - rayon, centerCircle.getY() - rayon, 2 * rayon, 2 * rayon);
        Shape shipShape = new Polygon(new int[]{0, 0, 12, 14, 8}, new int[]{0, 7, 8, 2, -2}, 5);
        assertFalse(Simulator.get().collision(shipShape, checkPointShape)); // Should be true
    }

    @Test
    void collisionCheckpointAndShipInsidePointContact() {
        double rayon = 5;
        PointDouble centerCircle = new PointDouble(-5, 5);
        Shape checkPointShape = new Ellipse2D.Double(centerCircle.getX() - rayon, centerCircle.getY() - rayon, 2 * rayon, 2 * rayon);
        Shape shipShape = new Polygon(new int[]{0, -2, 12, 14, 8}, new int[]{0, 4, 8, 2, -2}, 5);
        assertTrue(Simulator.get().collision(shipShape, checkPointShape)); // Should be true
    }

    @Test
    void collisionCheckpointAndShipInsideContact() {
        double rayon = 10;
        PointDouble centerCircle = new PointDouble(6, 6);
        Shape checkPointShape = new Ellipse2D.Double(centerCircle.getX() - rayon, centerCircle.getY() - rayon, 2 * rayon, 2 * rayon);
        Shape shipShape = new Polygon(new int[]{0, -2, 12, 14, 8}, new int[]{0, 4, 8, 2, -2}, 5);
        assertTrue(Simulator.get().collision(shipShape, checkPointShape));
    }

    @Test
    void collisionCheckpointAndShipInsideInverseContact() {
        double rayon = 1;
        PointDouble centerCircle = new PointDouble(6, 4);
        Shape checkPointShape = new Ellipse2D.Double(centerCircle.getX() - rayon, centerCircle.getY() - rayon, 2 * rayon, 2 * rayon);
        Shape shipShape = new Polygon(new int[]{0, -2, 12, 14, 8}, new int[]{0, 4, 8, 2, -2}, 5);
        assertTrue(Simulator.get().collision(shipShape, checkPointShape));
    }

    @Test
    void compareNull() {
        assertNotEquals(null, new Game());
    }
}
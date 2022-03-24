package fr.unice.polytech.si3.qgl.kihm.structures;

import fr.unice.polytech.si3.qgl.kihm.equipment.Oar;
import fr.unice.polytech.si3.qgl.kihm.landmarks.CheckPoint;
import fr.unice.polytech.si3.qgl.kihm.ship.Deck;
import fr.unice.polytech.si3.qgl.kihm.ship.Ship;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PathfinderTest {
    Pathfinder pf;
    Ship sh;
    CheckPoint c, backward, left, right, rudder, massive;
    ArrayList<CheckPoint> checkPoints;

    @BeforeEach
    void setUp() {
        sh = new Ship("s", 10, new Position(0, 0), new Rectangle2D.Double(10, 10, 20, 20), new Deck(3, 4), new ArrayList<>());
        pf = new Pathfinder(sh);
        c = new CheckPoint(new Position(300, 20, 0.5), new Rectangle2D.Double(10, 10, 20, 20));
        right = new CheckPoint(new Position(100, -200), new Rectangle2D.Double(6, -24, 8, 8));
        backward = new CheckPoint(new Position(-800, 0, 0), new Rectangle2D.Double(-10, -4, 8, 8));
        left = new CheckPoint(new Position(0, 200, 0), new Rectangle2D.Double(-4, 16, 8, 8));
        rudder = new CheckPoint(new Position(200, -20), new Rectangle2D.Double(196, -24, 8, 8));
        sh.setEquipments(List.of(new Oar(0, 0), new Oar(0, 2), new Oar(1, 0), new Oar(1, 2)));
        checkPoints = new ArrayList<>();
        checkPoints.add(c);
        checkPoints.add(backward);
        checkPoints.add(left);
        checkPoints.add(right);
        checkPoints.add(rudder);
        this.massive = new CheckPoint(new Position(10000, 10000), new Ellipse2D.Double(10000, 10000, 5000, 5000));
    }

    @Test
    void calculAngle() {
        assertEquals(0.06656816377582381, pf.calculAngle(c));
    }

    @Test
    void calculAngleBack() {
        assertEquals(3.141592653589793, pf.calculAngle(backward));
    }

    @Test
    void calculAngleLeft() {
        assertEquals(1.5707963267948966, pf.calculAngle(left));
    }

    @Test
    void equipmentNoSailors() {
        sh.setEquipments(List.of(new Oar(0, 0), new Oar(0, 2)));
        Map<String, Double> d = pf.getLayout(pf.calculAngle(c), 0);
        assertEquals(0, d.get("oarLeft"));
        assertEquals(0, d.get("oarRight"));
    }

    @Test
    void angleBehind() {
        Map<String, Double> d = pf.getLayout(pf.calculAngle(backward), 5);
        assertEquals(0, d.get("oarLeft"));
        assertEquals(2, d.get("oarRight"));
    }

    @Test
    void equipmentLeft() {
        Map<String, Double> d = pf.getLayout(pf.calculAngle(left), 5);
        assertEquals(0, d.get("oarLeft"));
        assertEquals(2, d.get("oarRight"));
    }

    @Test
    void angleRight() {
        Map<String, Double> d = pf.getLayout(pf.calculAngle(right), 5);
        assertEquals(2, d.get("oarLeft"));
        assertEquals(1, d.get("oarRight"));
    }

    @Test
    void rudderTest() {
        Map<String, Double> d = pf.getLayout(pf.calculAngle(rudder), 5);
        assertEquals(2, d.get("oarLeft"));
        assertEquals(2, d.get("oarRight"));
        assertEquals(0.0, d.get("rudder"));
    }

    @Test
    void algoTimeClosestPointTest() {
        long tempsDepart = System.currentTimeMillis();
        assertEquals(new Position(10732.27, 10732.2), pf.getClosestPoint(new Position(0, 0), massive.getShape()));
        assertTrue(System.currentTimeMillis() - tempsDepart <= 100);
    }

}
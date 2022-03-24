package fr.unice.polytech.si3.qgl.kihm.ship;

import fr.unice.polytech.si3.qgl.kihm.actions.Action;
import fr.unice.polytech.si3.qgl.kihm.actions.Moving;
import fr.unice.polytech.si3.qgl.kihm.actions.Turn;
import fr.unice.polytech.si3.qgl.kihm.equipment.Equipment;
import fr.unice.polytech.si3.qgl.kihm.equipment.Oar;
import fr.unice.polytech.si3.qgl.kihm.equipment.Rudder;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.unice.polytech.si3.qgl.kihm.actions.Action.actionTypeEnum.OAR;
import static java.lang.Math.PI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SailorManagerTest {
    Equipment oar1;
    Equipment oar2;
    Equipment oar3;
    Equipment rudder;
    List<Equipment> equipement;
    Ship ship;
    SailorManager sm;
    Sailor s1;
    Sailor s2;
    Sailor s3;
    Map<String, Double> layout;

    @BeforeEach
    void setUp() {
        this.layout = new HashMap<>();
        oar1 = new Oar(0, 0);
        oar2 = new Oar(0, 1);
        oar3 = new Oar(0, 2);
        rudder = new Rudder(3, 1);
        equipement = List.of(oar1, oar3,
                new Oar(1, 0), new Oar(1, 2),
                new Oar(2, 0), new Oar(2, 2),
                rudder);
        ship = new Ship("Le Royal Kihm", 500, new Position(0, 0),
                new Rectangle(2, 3), new Deck(3, 4),
                equipement);
        s1 = new Sailor(1, "1", new Point(0, 0));
        s2 = new Sailor(2, "2", new Point(0, 2));
        s3 = new Sailor(3, "3", new Point(3, 1));
        sm = new SailorManager(List.of(s1, s2, s3), ship);
    }

    @AfterEach
    void emptyLayout() {
        this.layout.clear();
    }

    @Test
    void assignRudder() {
        layout.put("oarLeft", 0.0);
        layout.put("oarRight", 0.0);
        layout.put("rudder", 0.5);
        assertEquals(List.of(new Turn(3, 0.5)), sm.assignRudder(layout, s3));
    }

    @Test
    void assignRudderOccupied() {
        rudder.setOccupied(true);
        layout.put("oarLeft", 0.0);
        layout.put("oarRight", 0.0);
        layout.put("rudder", 0.5);
        assertEquals(new ArrayList<>(), sm.assignRudder(layout, s3));
    }

    @Test
    void assignRudderNoOne() {
        layout.put("oarLeft", 0.0);
        layout.put("oarRight", 0.0);
        layout.put("rudder", 0.5);
        assertEquals(new ArrayList<>(), sm.assignRudder(layout, s2));
    }

    @Test
    void assignRudderEmpty() {
        layout.put("oarLeft", 0.0);
        layout.put("oarRight", 0.0);
        layout.put("rudder", 0.0);
        assertEquals(new ArrayList<>(), sm.assignRudder(layout, s3));
    }

    @Test
    void assignOarLeft() {
        layout.put("oarLeft", 0.0);
        layout.put("oarRight", 1.0);
        assertEquals(List.of(new Action(2, OAR)), sm.assignOars(layout, s2));

        layout.put("oarLeft", 1.0);
        layout.put("oarRight", 0.0);
        assertEquals(new ArrayList<>(), sm.assignOars(layout, s2));
    }

    @Test
    void assignOarRight() {
        layout.put("oarLeft", 1.0);
        layout.put("oarRight", 0.0);
        assertEquals(List.of(new Action(1, OAR)), sm.assignOars(layout, s1));

        layout.put("oarLeft", 0.0);
        layout.put("oarRight", 1.0);
        assertEquals(new ArrayList<>(), sm.assignOars(layout, s1));
    }

    @Test
    void assignOarOccupied() {
        oar1.setOccupied(true);
        layout.put("oarLeft", 1.0);
        layout.put("oarRight", 0.0);
        assertEquals(new ArrayList<>(), sm.assignOars(layout, s1));
    }

    @Test
    void assignOarNoOne() {
        layout.put("oarLeft", 0.0);
        layout.put("oarRight", 0.0);
        layout.put("rudder", 0.0);
        assertEquals(new ArrayList<>(), sm.assignOars(layout, s3));
    }

    @Test
    void assignOarEmpty() {
        layout.put("oarLeft", 0.0);
        layout.put("oarRight", 0.0);
        layout.put("rudder", 0.0);
        assertEquals(new ArrayList<>(), sm.assignOars(layout, s1));
    }

    @Test
    void whoUseEquipments() {
        layout.put("oarLeft", 1.0);
        layout.put("oarRight", 1.0);
        layout.put("rudder", 0.5);
        assertEquals(List.of(new Action(1, OAR), new Action(2, OAR), new Turn(3, .5)), sm.whoUseEquipments(layout));
    }

    @Test
    void whoUseEquipmentsAngleMax() {
        layout.put("oarLeft", 1.0);
        layout.put("oarRight", 1.0);
        layout.put("rudder", 4.0);
        assertEquals(List.of(new Action(1, OAR), new Action(2, OAR), new Turn(3, PI / 4)), sm.whoUseEquipments(layout));
    }

    @Test
    void sailorOnRudderNoMove() {
        this.layout.put("oarLeft", 1.0);
        this.layout.put("oarRight", 1.0);
        this.layout.put("rudder", 1.0);
        assertEquals(new ArrayList<>(), sm.moveSailors(this.layout));
    }

    @Test
    void sailorOnRudderMove() {
        this.layout.put("oarLeft", 1.0);
        this.layout.put("oarRight", 1.0);
        this.layout.put("rudder", 1.0);
        s3.setPosition(new Point(3, 0));
        assertEquals(List.of(new Moving(3, 0, 1)), sm.moveSailors(layout));
    }

    @Test
    void dontMoveSailors() {
        this.layout.put("oarLeft", 1.0);
        this.layout.put("oarRight", 1.0);
        this.s3.setPosition(new Point(2, 1));
        assertEquals(new ArrayList<>(), sm.moveSailors(layout));
    }

    @Test
    void testEquals() {
        assertNotEquals(new SailorManager(null, null), this.sm);
        assertEquals(new SailorManager(List.of(s1, s2, s3), this.ship), this.sm);
        assertEquals(this.sm, this.sm);
        assertNotEquals(new SailorManager(List.of(s1, s3), this.ship), this.sm);
    }

    @Test
    void testHashCode() {
        assertNotEquals(new Position().hashCode(), this.sm.hashCode());
        assertEquals(new SailorManager(List.of(s1, s2, s3), this.ship).hashCode(), this.sm.hashCode());
        assertEquals(this.sm.hashCode(), this.sm.hashCode());
        assertNotEquals(new SailorManager(List.of(s1, s3), this.ship).hashCode(), this.sm.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("{\"sailors\": " + List.of(s1, s2, s3) + ", \"ship\": " + this.ship + '}', this.sm.toString());
    }
}
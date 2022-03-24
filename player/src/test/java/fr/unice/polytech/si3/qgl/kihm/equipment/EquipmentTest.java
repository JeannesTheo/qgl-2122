package fr.unice.polytech.si3.qgl.kihm.equipment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EquipmentTest {
    Oar o, of;
    Rudder r;
    Sail s;

    @BeforeEach
    void setUp() {
        o = new Oar(0, 0);
        of = new Oar(0, 0);
        of.setOccupied(true);
        r = new Rudder(0, 0);
        s = new Sail(0, 0);
    }

    @Test
    void compareOarOccupied() {
        assertNotEquals(o, of);
    }

    @Test
    void compareNull() {
        assertNotEquals(null, o);
    }

    @Test
    void compareRudderOccupied() {
        Rudder rf = new Rudder(0, 0);
        rf.setOccupied(true);
        assertNotEquals(r, rf);
    }

    @Test
    void compareOar() {
        of.setOccupied(false);
        assertEquals(o, of);
    }

    @Test
    void compareSail() {
        Sail so = new Sail(0, 0, true);
        assertNotEquals(s, so);
    }

    @Test
    void compareSailPosition() {
        Sail so = new Sail(1, 0);
        assertNotEquals(s, so);
    }

    @Test
    void compareSailOar() {
        Equipment e1 = s;
        Equipment e2 = o;
        assertNotEquals(e1, e2);
    }

    @Test
    void getterX() {
        assertEquals(o.getX(), o.getPosition().getX());
    }

    @Test
    void getterY() {
        assertEquals(o.getY(), o.getPosition().getY());
    }

    @Test
    void testHashCode() {
        assertNotEquals(0, this.o.hashCode());
        assertNotEquals(this.of.hashCode(), this.o.hashCode());
        assertNotEquals(this.r.hashCode(), this.o.hashCode());
        assertNotEquals(new Rudder(4, 6).hashCode(), new Oar(4, 6).hashCode());
    }

    @Test
    void testType() {
        assertEquals(Equipment.equipmentTypeEnum.OAR, this.o.getType());
        assertEquals(Equipment.equipmentTypeEnum.RUDDER, this.r.getType());
    }

    @Test
    void testToString() {
        assertEquals("{\"type\": \"" + Equipment.equipmentTypeEnum.OAR.toString().toLowerCase() + "\", \"x\": " + this.o.getPosition().x + ", \"y\": " + this.o.getPosition().y + "}", this.o.toString());
    }
}
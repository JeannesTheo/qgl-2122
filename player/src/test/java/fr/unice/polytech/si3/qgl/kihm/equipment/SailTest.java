package fr.unice.polytech.si3.qgl.kihm.equipment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class SailTest {

    Sail sail;
    Point position;

    @BeforeEach
    void setUp() {
        this.position = new Point(6, 2);
        this.sail = new Sail(this.position.x, this.position.y);
    }

    @Test
    void testOpenAndCloseSail() {
        this.sail.closeSail();
        assertFalse(this.sail.isOpened());
        this.sail.openSail();
        this.sail.closeSail();
        this.sail.closeSail();
        this.sail.openSail();
        assertTrue(this.sail.isOpened());
        this.sail.closeSail();
    }

    @Test
    void testEquals() {
        assertEquals(this.sail, this.sail);
        assertEquals(new Sail(this.sail.getX(), this.sail.getY()), this.sail);
        assertNotEquals(new Sail(5, 7), this.sail);
        assertNotEquals(null, this.sail);
    }

    @Test
    void testHashCode() {
        assertEquals(this.sail.hashCode(), this.sail.hashCode());
        assertEquals(new Sail(this.sail.getX(), this.sail.getY()).hashCode(), this.sail.hashCode());
        assertNotEquals(new Sail(9, 2).hashCode(), this.sail.hashCode());
        assertNotEquals(0, this.sail.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("{\"type\": \"" + Equipment.equipmentTypeEnum.SAIL.toString().toLowerCase() + "\", \"x\": " + this.position.x + ", \"y\": " + this.position.y + ", \"opened\": " + this.sail.isOpened() + "}", this.sail.toString());
    }
}
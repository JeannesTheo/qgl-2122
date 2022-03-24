package fr.unice.polytech.si3.qgl.kihm.utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PositionTest {
    Position origin;
    Position north;
    Position north_east;
    Position east;
    Position south_east;
    Position south;
    Position south_west;
    Position west;
    Position north_west;

    @BeforeEach
    void setUp() {
        this.origin = new Position(Math.PI / 2); // Facing North
        this.north = new Position(0, 10, 0); // 0°
        this.north_east = new Position(10, 10, 0); // -45°
        this.east = new Position(10, 0, 0); // -90°
        this.south_east = new Position(10, -10, 0); // -135°
        this.south = new Position(0, -10, 0); // 180°
        this.south_west = new Position(-10, -10, 0); // 135°
        this.west = new Position(-10, 0, 0); // 90°
        this.north_west = new Position(-10, 10, 0); // 45°
    }

    @Test
    void testAngleFromFacingUp() {
        this.origin.setOrientation(Math.PI / 2);
        assertEquals(0, Math.toDegrees(this.origin.getAngleBetween(this.north)));
        assertEquals(-45, Math.toDegrees(this.origin.getAngleBetween(this.north_east)));
        assertEquals(-90, Math.toDegrees(this.origin.getAngleBetween(this.east)));
        assertEquals(-135, Math.toDegrees(this.origin.getAngleBetween(this.south_east)));
        assertEquals(180, Math.toDegrees(this.origin.getAngleBetween(this.south)));
        assertEquals(135, Math.toDegrees(this.origin.getAngleBetween(this.south_west)));
        assertEquals(90, Math.toDegrees(this.origin.getAngleBetween(this.west)));
        assertEquals(45, Math.toDegrees(this.origin.getAngleBetween(this.north_west)));
    }

    @Test
    void testAngleFromFacingDown() {
        this.origin.setOrientation(-1 * Math.PI / 2);
        assertEquals(180, Math.toDegrees(this.origin.getAngleBetween(this.north)));
        assertEquals(135, Math.toDegrees(this.origin.getAngleBetween(this.north_east)));
        assertEquals(90, Math.toDegrees(this.origin.getAngleBetween(this.east)));
        assertEquals(45, Math.toDegrees(this.origin.getAngleBetween(this.south_east)));
        assertEquals(0, Math.toDegrees(this.origin.getAngleBetween(this.south)));
        assertEquals(-45, Math.toDegrees(this.origin.getAngleBetween(this.south_west)));
        assertEquals(-90, Math.toDegrees(this.origin.getAngleBetween(this.west)));
        assertEquals(-135, Math.toDegrees(this.origin.getAngleBetween(this.north_west)));
    }

    @Test
    void testAngleFromFacingRight() {
        this.origin.setOrientation(0);
        assertEquals(90, Math.toDegrees(this.origin.getAngleBetween(this.north)));
        assertEquals(45, Math.toDegrees(this.origin.getAngleBetween(this.north_east)));
        assertEquals(0, Math.toDegrees(this.origin.getAngleBetween(this.east)));
        assertEquals(-45, Math.toDegrees(this.origin.getAngleBetween(this.south_east)));
        assertEquals(-90, Math.toDegrees(this.origin.getAngleBetween(this.south)));
        assertEquals(-135, Math.toDegrees(this.origin.getAngleBetween(this.south_west)));
        assertEquals(180, Math.toDegrees(this.origin.getAngleBetween(this.west)));
        assertEquals(135, Math.toDegrees(this.origin.getAngleBetween(this.north_west)));
    }

    @Test
    void testAngleFromFacingLeft() {
        this.origin.setOrientation(Math.PI);
        assertEquals(-90, Math.toDegrees(this.origin.getAngleBetween(this.north)));
        assertEquals(-135, Math.toDegrees(this.origin.getAngleBetween(this.north_east)));
        assertEquals(180, Math.toDegrees(this.origin.getAngleBetween(this.east)));
        assertEquals(135, Math.toDegrees(this.origin.getAngleBetween(this.south_east)));
        assertEquals(90, Math.toDegrees(this.origin.getAngleBetween(this.south)));
        assertEquals(45, Math.toDegrees(this.origin.getAngleBetween(this.south_west)));
        assertEquals(0, Math.toDegrees(this.origin.getAngleBetween(this.west)));
        assertEquals(-45, Math.toDegrees(this.origin.getAngleBetween(this.north_west)));
    }

    @Test
    void equalsPosition() {
        assertNotEquals(new Position(0, 0), new Position(0, 1));
        assertNotEquals(new Position(0, 0), new Position(1, 0));
        assertNotEquals(new Position(Math.PI / 2), new Position(Math.PI));
        assertNotEquals(null, new Position(0, 0));
        assertEquals(new Position(0, 2), new Position(0, 2));
    }

    @Test
    void testEquals() {
        assertNotEquals(new Position(), this.origin);
        assertEquals(new Position(this.origin.getX(), this.origin.getY(), this.origin.getOrientation()), this.origin);
        assertEquals(this.origin, this.origin);
        assertNotEquals(this.north, this.origin);
    }

    @Test
    void testHashCode() {
        assertNotEquals(new Position().hashCode(), this.origin.hashCode());
        assertNotEquals(new Position().hashCode(), this.south.hashCode());
        assertEquals(new Position(this.origin.getX(), this.origin.getY(), this.origin.getOrientation()).hashCode(), this.origin.hashCode());
        assertEquals(this.origin.hashCode(), this.origin.hashCode());
        assertNotEquals(this.north.hashCode(), this.origin.hashCode());
    }
}
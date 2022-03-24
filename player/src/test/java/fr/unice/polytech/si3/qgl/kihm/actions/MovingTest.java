package fr.unice.polytech.si3.qgl.kihm.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MovingTest {

    Moving moving;
    Moving movingDiag;

    @BeforeEach
    void setUp() {
        this.moving = new Moving(7, 8, 2);
        this.movingDiag = new Moving(7, 8, 8);
    }

    @Test
    void testGetters() {
        assertNotEquals(0, this.moving.getXdistance());
        assertEquals(0, this.moving.getYdistance());
        assertEquals(this.movingDiag.getXdistance(), this.moving.getXdistance());
        assertEquals(this.moving.getXdistance(), this.moving.getXdistance());
        assertNotEquals(this.movingDiag.getXdistance(), this.moving.getYdistance());
    }

    @Test
    void testHashCode() {
        assertNotEquals(0, new Action().hashCode());
        assertNotEquals(0, this.moving.hashCode());
        assertNotEquals(new Action(8, Action.actionTypeEnum.MOVING).hashCode(), this.moving.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("{\"sailorId\": " + this.moving.getSailorId() + ", \"type\": \"" + this.moving.getType() + "\", \"xdistance\": 5, \"ydistance\": 0" + '}', this.moving.toString());
        assertEquals("{\"sailorId\": " + this.movingDiag.getSailorId() + ", \"type\": \"" + this.movingDiag.getType() + "\", \"xdistance\": 5, \"ydistance\": 0" + '}', this.movingDiag.toString());
    }
}
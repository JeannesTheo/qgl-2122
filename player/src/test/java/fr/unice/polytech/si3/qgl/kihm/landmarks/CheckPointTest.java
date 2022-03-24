package fr.unice.polytech.si3.qgl.kihm.landmarks;

import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Ellipse2D;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CheckPointTest {
    CheckPoint c;

    @BeforeEach
    void setUp() {
        c = new CheckPoint(new Position());
    }

    @Test
    void checkPointNull() {
        assertNotEquals(null, c);
    }

    @Test
    void checkPointPostion() {
        CheckPoint cp = new CheckPoint(new Position(1, 0));
        assertNotEquals(c, cp);
    }

    @Test
    void checkPointShape() {
        CheckPoint cs = new CheckPoint(new Position(), new Ellipse2D.Double(10, 10, 10, 10));
        assertNotEquals(c, cs);
    }

    @Test
    void testHashCode() {
        CheckPoint cs = new CheckPoint(new Position(), new Ellipse2D.Double(10, 10, 10, 10));
        assertNotEquals(0, c.hashCode());
        assertNotEquals(0, cs.hashCode());
        assertNotEquals(c.hashCode(), cs.hashCode());
    }
}
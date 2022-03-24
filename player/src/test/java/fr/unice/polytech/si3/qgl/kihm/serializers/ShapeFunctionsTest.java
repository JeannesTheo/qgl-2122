package fr.unice.polytech.si3.qgl.kihm.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Area;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ShapeFunctionsTest {
    private JsonNode carreNode, carre45Node, triangleNode, triangle180Node, circleNode, circleNodeAngle, triangleNodeAngleD;
    private ShapeFunctions sf;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        sf = new ShapeFunctions();
        carreNode = om.readTree(
                "{\"type\":\"rectangle\"," +
                        "\"height\":10,\"width\":12,\"orientation\":0}");
        carre45Node = om.readTree(
                "{\"type\":\"rectangle\"," +
                        "\"height\":10,\"width\":12,\"orientation\":" + Math.PI / 4 + "}");
        triangleNode = om.readTree(
                "{\"type\":\"polygon\",\"vertices\":" +
                        "[{\"x\":" + -1 + ",\"y\":" + -1 + "}," +
                        "{\"x\":" + -1 + ",\"y\":" + 1 + "}," +
                        "{\"x\":" + 3 + ",\"y\":" + 0 + "}]," +
                        "\"orientation\":" + 0 + "}");
        triangle180Node = om.readTree(
                "{\"type\":\"polygon\",\"vertices\":" +
                        "[{\"x\":" + -1 + ",\"y\":" + -1 + "}," +
                        "{\"x\":" + -1 + ",\"y\":" + 1 + "}," +
                        "{\"x\":" + 3 + ",\"y\":" + 0 + "}]," +
                        "\"orientation\":" + Math.PI + "}");

        triangleNodeAngleD = om.readTree(
                "{\"type\":\"polygon\",\"vertices\":" +
                        "[{\"x\":" + -1 + ",\"y\":" + -1 + "}," +
                        "{\"x\":" + -1 + ",\"y\":" + 1 + "}," +
                        "{\"x\":" + 3 + ",\"y\":" + 0 + "}]," +
                        "\"orientation\":" + Math.PI / 2 + "}");
        circleNode = om.readTree(
                "{\"type\":\"circle\",\"radius\":" + 10 + ",\"orientation\":" + 0 + "}");

        circleNodeAngle = om.readTree(
                "{\"type\":\"circle\",\"radius\":" + 10 + ",\"orientation\":" + 2.15 + "}");
    }

    @Test
    void rectangleToShape() {
        Shape s = new Area();
        try {
            s = sf.nodeToShape(new Position(), carreNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            assertEquals(
                    """
                            6.0 5.0
                            6.0 -5.0
                            -6.0 -5.0
                            -6.0 5.0""", sf.shapeToString(s));
        }
    }

    @Test
    void rectangleAngleToShape() {
        Shape s = new Area();
        try {
            s = sf.nodeToShape(new Position(), carre45Node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            assertEquals(
                    """
                            0.7071099999999997 7.77821
                            7.77821 0.7071099999999997
                            -0.7071099999999997 -7.77821
                            -7.77821 -0.7071099999999997""", sf.shapeToString(s));
        }
    }


    @Test
    void circleToShape() {
        Shape s = new Area();
        try {
            s = sf.nodeToShape(new Position(10, 10, 0), circleNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            assertEquals(
                    "0.0 0.0\n" +
                            "20.0", sf.shapeToString(s));
        }
    }


    @Test
    void circleToShapeAngle() {
        Shape s = new Area();
        try {
            s = sf.nodeToShape(new Position(0, 0, 1.18), circleNodeAngle);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            assertEquals(
                    "-10.0 -10.0\n" +
                            "20.0", sf.shapeToString(s));
        }
    }

    @Test
    void triangleToNode() {
        Shape s = new Area();
        try {
            s = sf.nodeToShape(new Position(), triangleNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            assertEquals(
                    """
                            -1.0 -1.0
                            -1.0 1.0
                            3.0 0.0""", sf.shapeToString(s));
        }
    }

    @Test
    void triangleAngleToNode() {
        Shape s = new Area();
        try {
            s = sf.nodeToShape(new Position(), triangle180Node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            assertEquals(
                    """
                            1.0 1.0
                            1.0 -1.0
                            -3.0 0.0""", sf.shapeToString(s));
        }
    }

    @Test
    void triangleToNodeAngle() {
        Shape s = new Area();
        try {
            s = sf.nodeToShape(new Position(0, 0, Math.PI), triangleNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            assertEquals(
                    """
                            1.0 1.0
                            1.0 -1.0
                            -3.0 0.0""", sf.shapeToString(s));
        }
    }

    @Test
    void triangleToNodeAngleCumul() {
        Shape s = new Area();
        try {
            s = sf.nodeToShape(new Position(0, 0, Math.PI / 2), triangleNodeAngleD);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            assertEquals(
                    """
                            1.0 1.0
                            1.0 -1.0
                            -3.0 0.0""", sf.shapeToString(s));
        }
    }

    @Test
    void areaShape() {
        Shape s = new Area();
        assertEquals("", sf.shapeToString(s));
    }
}
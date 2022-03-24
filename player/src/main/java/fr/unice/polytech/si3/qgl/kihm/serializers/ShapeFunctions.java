package fr.unice.polytech.si3.qgl.kihm.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si3.qgl.kihm.utilities.PointDouble;
import fr.unice.polytech.si3.qgl.kihm.utilities.Position;

import java.awt.*;
import java.awt.geom.*;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * Functions to deal with transforming nodes into shapes
 */
public class ShapeFunctions {
    private static final String OR = "orientation";
    private final ObjectMapper om;


    public ShapeFunctions() {
        om = new ObjectMapper();
    }

    public Shape nodeToShape(Position p, JsonNode node) throws JsonProcessingException {
        Shape shape;
        String s = isNull(node.get("type")) ? "" : node.get("type").asText();
        shape = switch (s) {
            case "polygon" -> nodeToPolygon(p, node);
            case "circle" -> nodeToCircle(p, node);
            case "rectangle" -> nodeToPolygon(p, rectangleToPolygon(node));
            default -> new Area();
        };
        return shape;
    }

    /**
     * Transform a polygon-typed node into a Path2D
     *
     * @param position center and orientation of the shape
     * @param s        the node
     * @return a closed path representing the shape
     * @throws JsonProcessingException if readValue fails
     */
    private Path2D.Double nodeToPolygon(Position position, JsonNode s) throws JsonProcessingException {
        double orientation = position.getOrientation() + (isNull(s.get(OR)) ? 0 : s.get(OR).asDouble());
        double rounded = Math.pow(10, 5);
        double cos = Math.round(Math.cos(orientation) * rounded) / rounded;
        double sin = Math.round(Math.sin(orientation) * rounded) / rounded;
        //Calcul de la matrice de rotation
        double[][] matOrientation = {{cos, -sin}, {sin, cos}};
        List<PointDouble> vertices = om.readValue(s.get("vertices").toString(), new TypeReference<>() {
        });
        //On modifie chaque point, d'abord en l'orientant
        for (PointDouble p : vertices) {
            p.multiplyMatrix(matOrientation);
            //Puis en le translatant depuis l'origine
            p.move(position.getX(), position.getY());
        }
        //Construction du chemin
        Path2D.Double path = new Path2D.Double();
        path.moveTo(vertices.get(0).getX(), vertices.get(0).getY());
        if (!vertices.isEmpty())
            vertices.remove(0);
        vertices.forEach(e -> path.lineTo(e.getX(), e.getY()));
        path.closePath();
        return path;
    }

    /**
     * Transform a shape into a list of points
     *
     * @param shape any class implementing Shape
     * @return list of points
     */
    public String shapeToString(Shape shape) {
        String res;
        if (shape instanceof Ellipse2D.Double s)
            res = circleToString(s);
        else
            res = polygonToString(shape);
        return res;
    }

    /**
     * @param shape a polygon
     * @return list of points
     */
    private String polygonToString(Shape shape) {
        StringBuilder res = new StringBuilder();
        PathIterator p = shape.getPathIterator(new AffineTransform());
        while (!p.isDone()) {
            double[] tmp = new double[2];
            p.currentSegment(tmp);
            for (int i = 0; i < tmp.length; i += 2) {
                res.append(tmp[i]).append(" ").append(tmp[i + 1]).append("\n");
            }
            p.next();
        }
        if (!res.isEmpty()) {//Enlever le dernier retour a la ligne
            res.deleteCharAt(res.length() - 1);
        }
        return res.substring(0, Math.max(0, res.lastIndexOf("\n")));//Enlever la derniere ligne (0 0)
    }

    /**
     * @param circle an Ellipse2D
     * @return X and Y coords, then the diameter
     */
    private String circleToString(Ellipse2D.Double circle) {
        return circle.getX() + " " + circle.getY() + "\n" + circle.getHeight();
    }

    /**
     * Transform a rectangle-typed node into a polygon-typed node
     *
     * @param s rectangle
     * @return polygon
     * @throws JsonProcessingException if readTree fails
     */
    private JsonNode rectangleToPolygon(JsonNode s) throws JsonProcessingException {
        double orientation = (isNull(s.get(OR)) ? 0 : s.get(OR).asDouble());
        double width = (isNull(s.get("width")) ? 0 : s.get("width").asDouble());
        double height = (isNull(s.get("height")) ? 0 : s.get("height").asDouble());
        String x = "\"x\":";
        String y = ",\"y\":";
        //Verification que les valeurs existent, 0 sinon
        return new ObjectMapper().readTree(
                "{\"type\":\"polygon\",\"vertices\":" +
                        "[{" + x + (width / 2) + y + (height / 2) + "}," +
                        "{" + x + (width / 2) + y + -(height / 2) + "}," +
                        "{" + x + -(width / 2) + y + -(height / 2) + "}," +
                        "{" + x + -(width / 2) + y + (height / 2) + "}]," +
                        "\"orientation\":" + orientation + "}");
    }

    /**
     * Transform a circle-typed node into an Ellipse2D
     *
     * @param p center and orientation of the shape
     * @param s the node
     * @return an Ellipse representing the shape
     */
    private Ellipse2D.Double nodeToCircle(Position p, JsonNode s) {
        double radius = isNull(s.get("radius")) ? 0 : s.get("radius").asDouble();
        //Verification que les valeurs existent, 0 sinon
        return new Ellipse2D.Double(p.getX() - radius, p.getY() - radius, radius * 2, radius * 2);
    }
}

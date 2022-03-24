package fr.unice.polytech.si3.qgl.kihm.landmarks;

import fr.unice.polytech.si3.qgl.kihm.utilities.Position;

import java.awt.*;

import static fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle.obstacleTypeEnum.REEF;

public class Obstacle {

    protected final Position position;
    protected final Shape shape;
    protected final obstacleTypeEnum type;

    public Obstacle(Position position, Shape shape) {
        this.position = position;
        this.shape = shape;
        this.type = REEF;
    }

    public Obstacle(obstacleTypeEnum type, Position position, Shape shape) {
        this.position = position;
        this.shape = shape;
        this.type = type;
    }

    public Position getPosition() {
        return position;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public Shape getShape() {
        return shape;
    }

    public obstacleTypeEnum getType() {
        return type;
    }

    @Override
    public String toString() {
        return "{\"type\": \"" + this.type.toString().toLowerCase() + "\", \"position\": " + this.position + ", \"shape\": " + this.shape.getBounds2D() + "}";
    }

    public enum obstacleTypeEnum {
        STREAM,
        SHIP,
        REEF,
        CHECKPOINT
    }
}

package fr.unice.polytech.si3.qgl.kihm.landmarks;

import fr.unice.polytech.si3.qgl.kihm.utilities.Position;

import java.awt.*;

import static fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle.obstacleTypeEnum.STREAM;

public class Stream extends Obstacle {

    private final double strength;

    public Stream(Position position, Shape shape, double strength) {
        super(STREAM, position, shape);
        this.strength = strength;
    }

    public double getStrength() {
        return strength;
    }

    @Override
    public String toString() {
        return "{\"type\": \"" + this.getType().toString().toLowerCase() + "\", \"position\": " + this.position + ", \"shape\": " + this.shape.getBounds2D() + ", \"strength\": " + this.strength + "}";
    }
}

package fr.unice.polytech.si3.qgl.kihm.landmarks;

import fr.unice.polytech.si3.qgl.kihm.utilities.Position;

import java.awt.*;
import java.awt.geom.Area;
import java.util.Objects;

import static fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle.obstacleTypeEnum.CHECKPOINT;

public class CheckPoint extends Obstacle {

    public CheckPoint(Position position) {
        this(position, new Area());
    }

    public CheckPoint(Position position, Shape shape) {
        super(CHECKPOINT, position, shape);
    }

    public double distance(Position position) {
        return this.getPosition().distance(position);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CheckPoint checkPoint = (CheckPoint) o;
        return Objects.equals(position, checkPoint.position) && Objects.equals(shape.getBounds2D(), checkPoint.shape.getBounds2D());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

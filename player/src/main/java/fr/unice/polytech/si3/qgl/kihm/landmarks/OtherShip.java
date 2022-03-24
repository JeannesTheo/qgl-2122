package fr.unice.polytech.si3.qgl.kihm.landmarks;

import fr.unice.polytech.si3.qgl.kihm.utilities.Position;

import java.awt.*;

import static fr.unice.polytech.si3.qgl.kihm.landmarks.Obstacle.obstacleTypeEnum.SHIP;

public class OtherShip extends Obstacle {

    private final int life;

    public OtherShip(Position position, Shape shape, int life) {
        super(SHIP, position, shape);
        this.life = life;
    }

    public int getLife() {
        return life;
    }

    @Override
    public String toString() {
        return "{\"type\": \"" + this.getType().toString().toLowerCase() + "\", \"position\": " + this.position + ", \"shape\": " + this.shape.getBounds2D() + ", \"life\": " + this.life + "}";
    }
}

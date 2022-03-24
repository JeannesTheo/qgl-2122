package fr.unice.polytech.si3.qgl.kihm.equipment;

import lombok.Data;

import java.awt.*;
import java.util.Objects;

@Data
public abstract class Equipment {
    public static final String OAR_RIGHT = "oarRight";
    public static final String OAR_LEFT = "oarLeft";
    public static final String RUDDER = "rudder";
    public static final String SAIL = "sail";
    
    private final Point position;
    private final equipmentTypeEnum type;
    protected boolean isOccupied = false;

    protected Equipment(equipmentTypeEnum type, int x, int y) {
        position = new Point(x, y);
        this.type = type;
    }

    public Point getPosition() {
        return position;
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return isOccupied == equipment.isOccupied && Objects.equals(position, equipment.position) && type == equipment.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, type, isOccupied);
    }

    @Override
    public String toString() {
        return "{\"type\": \"" + this.type.toString().toLowerCase() + "\", \"x\": " + this.getX() + ", \"y\": " + this.getY() + "}";
    }

    public enum equipmentTypeEnum {
        OAR,
        CANON,
        RUDDER,
        SAIL,
        WATCH
    }
}

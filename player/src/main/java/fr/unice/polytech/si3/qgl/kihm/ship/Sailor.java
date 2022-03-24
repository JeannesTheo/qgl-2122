package fr.unice.polytech.si3.qgl.kihm.ship;

import fr.unice.polytech.si3.qgl.kihm.equipment.Equipment;

import java.awt.*;
import java.util.Objects;

public class Sailor {

    public final int id;
    public final String name;
    private Point position;

    public Sailor() {
        id = 0;
        name = "Default";
        position = new Point(0, 0);
    }

    public Sailor(int id, String name) {
        this.id = id;
        this.name = name;
        position = new Point(0, 0);
    }

    public Sailor(int id, String name, Point position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    public int[] moveTo(Equipment equipment) {
        return this.moveTo(equipment.getPosition());
    }

    public int[] moveTo(Point pos) {
        return new int[]{pos.x - this.position.x, pos.y - this.position.y};
    }

    public int distanceTo(Equipment equipment) {
        return Math.abs(equipment.getX() - this.position.x) + Math.abs(equipment.getY() - this.position.y);
    }

    public int getId() {
        return id;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sailor sailor = (Sailor) o;
        return id == sailor.id && Objects.equals(name, sailor.name) && Objects.equals(position, sailor.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position);
    }

    @Override
    public String toString() {
        return "{\"id\": " + this.id + ", \"x\": " + this.getPosition().x + ", \"y\": " + this.getPosition().y + ", \"name\": " + this.name + "}";
    }
}

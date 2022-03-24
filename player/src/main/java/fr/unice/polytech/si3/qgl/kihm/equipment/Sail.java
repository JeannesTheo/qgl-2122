package fr.unice.polytech.si3.qgl.kihm.equipment;

import java.util.Objects;

public class Sail extends Equipment {
    private boolean opened = false;

    public Sail(int x, int y) {
        super(equipmentTypeEnum.SAIL, x, y);
    }

    public Sail(int x, int y, boolean opened) {
        this(x, y);
        this.opened = opened;
    }

    public boolean isOpened() {
        return opened;
    }

    public void openSail() {
        this.opened = true;
    }

    public void closeSail() {
        this.opened = false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sail sail = (Sail) o;
        return opened == sail.opened && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), opened);
    }

    @Override
    public String toString() {
        return "{\"type\": \"" + this.getType().toString().toLowerCase() + "\", \"x\": " + this.getX() + ", \"y\": " + this.getY() + ", \"opened\": " + this.isOpened() + "}";
    }
}

package fr.unice.polytech.si3.qgl.kihm.actions;

import java.util.Objects;

import static fr.unice.polytech.si3.qgl.kihm.ship.SailorManager.MOVEMENT;

/**
 * Action Moving, allow a sailor to move on the deck
 */
public class Moving extends Action {
    private final int xdistance;
    private final int ydistance;

    /**
     * Initialization of Moving Action.
     * If absolute distance of x and y is greater than 5, x and y will be 0
     *
     * @param sailorId id of the sailor to move
     * @param x        x-displacement
     * @param y        y-displacement
     */
    public Moving(int sailorId, int x, int y) {
        super(sailorId, actionTypeEnum.MOVING);
        int distance = Math.abs(x) + Math.abs(y);
        if (distance > MOVEMENT) {
            boolean xNegatif = x < 0;
            boolean yNegatif = y < 0;
            x = Math.min(Math.abs(x), MOVEMENT);
            y = MOVEMENT - x;
            x = xNegatif ? -x : x;
            y = yNegatif ? -y : y;
        }
        this.xdistance = x;
        this.ydistance = y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Moving moving = (Moving) o;
        return xdistance == moving.xdistance && ydistance == moving.ydistance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), xdistance, ydistance);
    }

    public int getXdistance() {
        return xdistance;
    }

    public int getYdistance() {
        return ydistance;
    }

    @Override
    public String toString() {
        return "{\"sailorId\": " + this.sailorId + ", \"type\": \"MOVING\", \"xdistance\": " + this.xdistance + ", \"ydistance\": " + this.ydistance + '}';
    }
}

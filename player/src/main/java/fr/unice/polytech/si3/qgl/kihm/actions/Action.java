package fr.unice.polytech.si3.qgl.kihm.actions;

import java.util.Objects;

/**
 * Create Actions to be serialized
 */
public class Action {
    private final actionTypeEnum type;
    protected int sailorId;

    public Action() {
        this.sailorId = 0;
        this.type = null;
    }

    public Action(int sailorId, actionTypeEnum type) {
        this.sailorId = sailorId;
        this.type = type;
    }

    public int getSailorId() {
        return sailorId;
    }

    public actionTypeEnum getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return sailorId == action.sailorId && type == action.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sailorId, type);
    }

    @Override
    public String toString() {
        return "{\"sailorId\": " + this.sailorId + ", \"type\": \"" + this.type + "\"}";
    }

    public enum actionTypeEnum {
        MOVING,
        LIFT_SAIL,
        LOWER_SAIL,
        TURN,
        OAR,
        USE_WATCH,
        AIM,
        FIRE,
        RELOAD
    }
}

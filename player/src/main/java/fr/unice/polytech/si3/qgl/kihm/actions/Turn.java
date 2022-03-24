package fr.unice.polytech.si3.qgl.kihm.actions;


import java.util.Objects;

import static java.lang.Math.PI;

public class Turn extends Action {
    private final double rotation;

    /**
     * Turn must be between -PI/4 and PI/4. If not, it will be rounded to the closest value allowed, either -PI/4 or PI/4
     *
     * @param id    ID of the sailor
     * @param angle Rotation of the rudder
     */
    public Turn(int id, double angle) {
        super(id, actionTypeEnum.TURN);
        sailorId = id;
        if (angle < -(PI / 4))
            angle = -(PI / 4);
        else if (angle > PI / 4)
            angle = PI / 4;
        this.rotation = angle;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Turn turn = (Turn) o;
        return Double.compare(turn.rotation, rotation) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rotation);
    }

    public double getRotation() {
        return rotation;
    }

    @Override
    public String toString() {
        return "{\"sailorId\": " + this.sailorId + ", \"type\": \"" + this.getType() + "\", \"rotation\": " + this.rotation + '}';
    }
}

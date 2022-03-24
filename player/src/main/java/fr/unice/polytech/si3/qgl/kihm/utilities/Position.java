package fr.unice.polytech.si3.qgl.kihm.utilities;

import lombok.Data;

@Data
public class Position extends PointDouble {
    private double orientation = 0;

    public Position() {
    }

    public Position(double x, double y) {
        super(x, y);
    }

    public Position(double orientation) {
        super();
        this.orientation = orientation;
    }

    public Position(double x, double y, double orientation) {
        super(x, y);
        this.orientation = orientation;
    }

    public void rotate(double theta) {
        this.setOrientation(this.getOrientation() + theta);
    }

    /**
     * Compute angle between a moving object e.g a ship, and a non-moving point, e.g a checkpoint
     *
     * @param p Position of the non-moving object
     * @return angle needed to align the moving object on the non-moving one
     */
    public double getAngleBetween(Position p) {
        this.orientation %= 2 * Math.PI;
        double a = p.getX() - this.getX();
        double b = p.getY() - this.getY();
        double theta = Math.atan2(b, a) - this.orientation;
        if (theta > Math.PI) {
            theta -= 2 * Math.PI;
        }
        if (theta < -1 * Math.PI) {
            theta += 2 * Math.PI;
        }
        return theta == -1 * Math.PI ? Math.PI : theta;
    }

    public double distance(Position position) {
        return Math.sqrt(Math.pow(this.getX() - position.getX(), 2) + Math.pow(this.getY() - position.getY(), 2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Position position = (Position) o;

        return Double.compare(position.orientation, orientation) == 0;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(orientation);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "{\"x\": " + this.getX() + ", \"y\": " + this.getY() + ", \"orientation\": " + orientation + '}';
    }
}

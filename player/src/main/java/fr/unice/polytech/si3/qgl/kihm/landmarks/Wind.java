package fr.unice.polytech.si3.qgl.kihm.landmarks;

import lombok.Data;

@Data
public class Wind {
    private final double orientation;
    private final double strength;

    public Wind() {
        orientation = 0;
        strength = 0;
    }

    public Wind(double orientation, double strength) {
        this.orientation = orientation;
        this.strength = strength;
    }

    public double getOrientation() {
        return orientation;
    }

    public double getStrength() {
        return strength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wind wind = (Wind) o;
        return Double.compare(wind.orientation, orientation) == 0 && Double.compare(wind.strength, strength) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(orientation);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(strength);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "{\"orientation\": " + this.orientation + ", \"strength\": " + this.strength + "}";
    }
}

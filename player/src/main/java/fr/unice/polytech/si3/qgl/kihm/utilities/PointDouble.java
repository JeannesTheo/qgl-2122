package fr.unice.polytech.si3.qgl.kihm.utilities;

import lombok.Data;

/**
 * Create a Point with x,y coords typed as double
 */
@Data
public class PointDouble {
    private double x = 0;
    private double y = 0;

    public PointDouble() {
    }

    public PointDouble(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * set x and y to the coords of the points
     *
     * @param x x-position
     * @param y y-position
     */
    public void moveTo(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * add x and y to the coords of the points
     *
     * @param x x-movement
     * @param y y-movement
     */
    public void move(double x, double y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Add the x value to x
     *
     * @param x x-movement
     */
    public void moveX(double x) {
        this.x += x;
    }

    /**
     * Add the y value to y
     *
     * @param y y-movement
     */
    public void moveY(double y) {
        this.y += y;
    }

    /**
     * Multiply the coords by a 2-2 matrix,
     * representing x and y as a column matrix
     * as follows : [2-2 Matrix]*[x-y]
     *
     * @param m 2-2 matrix
     */
    public void multiplyMatrix(double[][] m) {
        double tx = x;
        double ty = y;
        if (m.length == 2 && m[0].length == 2) {
            x = m[0][0] * tx + m[0][1] * ty;
            y = m[1][0] * tx + m[1][1] * ty;
        }
    }

    @Override
    public String toString() {
        return "{\"x\": " + this.x + ", \"y\": " + this.y + "}";
    }
}

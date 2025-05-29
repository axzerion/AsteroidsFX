package dk.sdu.mmmi.cbse.common.asteroids;

import dk.sdu.mmmi.cbse.common.data.Entity;

/**
 *
 * @author corfixen
 */
public class Asteroid extends Entity {
    public enum Size {

        SMALL(10),
        MEDIUM(15),
        LARGE(20);

        private final int radius;

        Size(int radius) {
            this.radius = radius;
        }

        public int getRadius() {
            return radius;
        }   
    }

    private Size size;
    private double dx;
    private double dy;
    private double rotationSpeed;


    public Asteroid() {
        this.size = Size.LARGE; // Default size
        this.dx = 0;
        this.dy = 0;
        this.rotationSpeed = 0;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(double rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    // Method for splitting
    public Size getNextSize() {
        switch (size) {
            case LARGE:
                return Size.MEDIUM; // WILL SPLIT INTO MEDIUM
            case MEDIUM:
                return Size.SMALL; // WILL SPLIT INTO SMALL
            default:
                return null; // WILL NOT SPLIT
        }
    }
}
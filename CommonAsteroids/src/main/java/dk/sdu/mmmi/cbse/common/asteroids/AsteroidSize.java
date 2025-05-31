package dk.sdu.mmmi.cbse.common.asteroids;

public enum AsteroidSize {
    LARGE(50, 0.8),
    MEDIUM(30, 1.5),
    SMALL(15, 2.5);

    private final int radius;
    private final double speedMultiplier;

    AsteroidSize(int radius, double speedMultiplier) {
        this.radius = radius;
        this.speedMultiplier = speedMultiplier;
    }

    public int getRadius() {
        return radius;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }
}
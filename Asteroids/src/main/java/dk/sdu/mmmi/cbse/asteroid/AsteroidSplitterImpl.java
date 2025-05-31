package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.AsteroidSize;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

public class AsteroidSplitterImpl implements IAsteroidSplitter {

    @Override
    public void createSplitAsteroid(Entity e, World world) {
        if (!(e instanceof Asteroid asteroid)) return;

        AsteroidSize currentSize = asteroid.getSize();
        AsteroidSize newSize = null;

        if (currentSize == AsteroidSize.LARGE) {
            newSize = AsteroidSize.MEDIUM;
        } else if (currentSize == AsteroidSize.MEDIUM) {
            newSize = AsteroidSize.SMALL;
        } else {
            // SMALL asteroids should not split further
            return;
        }

        for (int i = 0; i < 2; i++) {
            Asteroid newAsteroid = new Asteroid();
            newAsteroid.setSize(newSize);

            int radius = newSize.getRadius();
            newAsteroid.setRadius(radius);

            // Polygon shape
            newAsteroid.setPolygonCoordinates(
                    radius, 0,
                    radius / 2, radius * 0.866,
                    -radius / 2, radius * 0.866,
                    -radius, 0,
                    -radius / 2, -radius * 0.866,
                    radius / 2, -radius * 0.866
            );

            // Small random offset to avoid overlapping
            double offset = 10 * (Math.random() - 0.5);
            newAsteroid.setX(asteroid.getX() + offset);
            newAsteroid.setY(asteroid.getY() + offset);

            // Random rotation
            newAsteroid.setRotation(Math.random() * 360);

            world.addEntity(newAsteroid);
        }
    }
}

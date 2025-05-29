package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;
import java.util.Random;

public class AsteroidSplitterImpl implements IAsteroidSplitter {
    private final Random random = new Random();

    @Override
    public void createSplitAsteroid(Entity e, World world) {
        Asteroid asteroid = (Asteroid) e;
        Asteroid.Size currentSize = asteroid.getSize();
        Asteroid.Size nextSize = getNextSize(currentSize);
        
        if (nextSize == null) {
            world.removeEntity(asteroid);
            return;
        }

        // Create two new asteroids
        for (int i = 0; i < 2; i++) {
            Asteroid newAsteroid = new Asteroid();
            newAsteroid.setSize(nextSize);
            newAsteroid.setRadius(nextSize.getRadius());
            
            // Position new asteroid
            double offset = nextSize.getRadius() * 2;
            double angle = asteroid.getRotation() + (i == 0 ? -45 : 45);
            newAsteroid.setX(asteroid.getX() + Math.cos(Math.toRadians(angle)) * offset);
            newAsteroid.setY(asteroid.getY() + Math.sin(Math.toRadians(angle)) * offset);
            
            // Set movement properties
            newAsteroid.setDx(Math.cos(Math.toRadians(angle)) * 0.3);
            newAsteroid.setDy(Math.sin(Math.toRadians(angle)) * 0.3);
            newAsteroid.setRotationSpeed(random.nextDouble() * 0.3);
            
            world.addEntity(newAsteroid);
        }
        
        world.removeEntity(asteroid);
    }

    private Asteroid.Size getNextSize(Asteroid.Size currentSize) {
        return switch (currentSize) {
            case LARGE -> Asteroid.Size.MEDIUM;
            case MEDIUM -> Asteroid.Size.SMALL;
            case SMALL -> null;
        };
    }
} 
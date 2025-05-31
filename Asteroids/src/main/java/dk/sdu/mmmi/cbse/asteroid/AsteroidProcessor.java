package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.asteroids.AsteroidSize;
import java.util.Random;

public class AsteroidProcessor implements IEntityProcessingService {

    private final Random random = new Random();
    private long lastSpawnTime = 0;

    @Override
    public void process(GameData gameData, World world) {
        // Spawn new asteroid every second
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSpawnTime >= 1000) { // 1000ms = 1 second
            Entity asteroid = createAsteroid(gameData, AsteroidSize.LARGE);
            world.addEntity(asteroid);
            lastSpawnTime = currentTime;
        }

        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            // Get the constant movement based on rotation
            double changeX = Math.cos(Math.toRadians(asteroid.getRotation()));
            double changeY = Math.sin(Math.toRadians(asteroid.getRotation()));

            // Move in that direction
            asteroid.setX(asteroid.getX() + changeX * 0.5);
            asteroid.setY(asteroid.getY() + changeY * 0.5);

            // Screen wrapping logic
            if (asteroid.getX() < 0) {
                asteroid.setX(gameData.getDisplayWidth());
            }
            if (asteroid.getX() > gameData.getDisplayWidth()) {
                asteroid.setX(0);
            }
            if (asteroid.getY() < 0) {
                asteroid.setY(gameData.getDisplayHeight());
            }
            if (asteroid.getY() > gameData.getDisplayHeight()) {
                asteroid.setY(0); 
            }
        }
    }

    private Entity createAsteroid(GameData gameData, AsteroidSize size) {
        Entity asteroid = new Asteroid();
        ((Asteroid)asteroid).setSize(size);
        int radius = size.getRadius();

        asteroid.setPolygonCoordinates(
            radius, 0,
            (double) radius /2, radius*0.866,
            (double) -radius /2, radius*0.866,
            -radius, 0,
            (double) -radius /2, -radius*0.866,
            (double) radius /2, -radius*0.86
        );

        asteroid.setRadius(radius);

        // Set random position at screen edges
        if (random.nextBoolean()) {
            // Spawn on left or right edge
            asteroid.setX(random.nextBoolean() ? 0 : gameData.getDisplayWidth());
            asteroid.setY(random.nextInt(gameData.getDisplayHeight()));
        } else {
            // Spawn on top or bottom edge
            asteroid.setX(random.nextInt(gameData.getDisplayWidth()));
            asteroid.setY(random.nextBoolean() ? 0 : gameData.getDisplayHeight());
        }

        // Set random rotation
        asteroid.setRotation(Math.random() * 360);

        return asteroid;
    }

    /**
     * Dependency Injection using OSGi Declarative Services
     */
    public void setAsteroidSplitter(IAsteroidSplitter asteroidSplitter) {
    }

    public void removeAsteroidSplitter(IAsteroidSplitter asteroidSplitter) {
    }
}

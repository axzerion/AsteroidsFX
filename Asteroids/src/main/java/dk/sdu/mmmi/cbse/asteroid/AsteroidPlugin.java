package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.asteroids.AsteroidSize;
import java.util.Random;


public class AsteroidPlugin implements IGamePluginService {

    private final Random random = new Random();

    // Creates initial asteroids
    @Override
    public void start(GameData gameData, World world) {
        for (int i = 0; i < 20; i++) {
            Entity asteroid = createAsteroid(gameData);
            world.addEntity(asteroid);
        }
    }

    
    private Entity createAsteroid(GameData gameData) {
        Asteroid asteroid = new Asteroid();
        asteroid.setSize(AsteroidSize.LARGE);
        int radius = AsteroidSize.LARGE.getRadius();
        
        asteroid.setPolygonCoordinates(
            radius, 0,
            (double) radius /2, radius*0.866,
            (double) -radius /2, radius*0.866,
            -radius, 0,
            (double) -radius /2, -radius*0.866,
            (double) radius /2, -radius*0.866
        );
        
        asteroid.setRadius(radius);

        // Set a random position at screen edges
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

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            world.removeEntity(asteroid);

        }
    }
}

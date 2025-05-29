package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.Random;

/**
 *
 * @author corfixen
 */
public class AsteroidPlugin implements IGamePluginService {
    private static final int INITIAL_ASTEROIDS = 5; // Number of asteroids to spawn initially
    private Random random = new Random();

    @Override
    public void start(GameData gameData, World world) {
        // Spawn multiple asteroids
        for (int i = 0; i < INITIAL_ASTEROIDS; i++) {
            world.addEntity(createAsteroid(gameData));
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        world.getEntities(Asteroid.class).forEach(world::removeEntity);
    }

    private Entity createAsteroid(GameData gameData) {
        Asteroid asteroid = new Asteroid();
        
        // Simple size distribution
        Asteroid.Size size = random.nextDouble() < 0.5 ? Asteroid.Size.LARGE : 
                           random.nextDouble() < 0.6 ? Asteroid.Size.MEDIUM : 
                           Asteroid.Size.SMALL;
        
        asteroid.setSize(size);
        int radius = size.getRadius();
        asteroid.setRadius(radius);
        
        // Create hexagon
        double[] coordinates = new double[12];
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i);
            coordinates[i * 2] = radius * Math.cos(angle);
            coordinates[i * 2 + 1] = radius * Math.sin(angle);
        }
        asteroid.setPolygonCoordinates(coordinates);
        
        // Position and movement
        asteroid.setX(random.nextDouble() * gameData.getDisplayWidth());
        asteroid.setY(random.nextDouble() * gameData.getDisplayHeight());
        asteroid.setRotation(random.nextDouble() * 360);
        
        // Speed based on size
        double speed = 0.3 * (size == Asteroid.Size.SMALL ? 1.3 : 
                            size == Asteroid.Size.MEDIUM ? 1.0 : 0.7);
        
        double angle = Math.toRadians(random.nextDouble() * 360);
        asteroid.setDx(Math.cos(angle) * speed);
        asteroid.setDy(Math.sin(angle) * speed);
        asteroid.setRotationSpeed((random.nextDouble() - 0.5) * 0.3);
        
        return asteroid;
    }
}

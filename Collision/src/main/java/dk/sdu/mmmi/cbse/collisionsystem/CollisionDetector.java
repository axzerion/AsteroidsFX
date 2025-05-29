package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import java.util.ServiceLoader;

public class CollisionDetector implements IPostEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity1 : world.getEntities()) {
            for (Entity entity2 : world.getEntities()) {
                if (entity1.getID().equals(entity2.getID())) {
                    continue;
                }

                if (this.collides(entity1, entity2)) {
                    // Handle asteroid-bullet collision
                    if (entity1 instanceof Asteroid && entity2 instanceof Bullet) {
                        handleAsteroidBulletCollision(entity1, entity2, world);
                    } else if (entity1 instanceof Bullet && entity2 instanceof Asteroid) {
                        handleAsteroidBulletCollision(entity2, entity1, world);
                    } else {
                        // Handle other collisions
                        world.removeEntity(entity1);
                        world.removeEntity(entity2);
                    }
                }
            }
        }
    }

    private void handleAsteroidBulletCollision(Entity asteroid, Entity bullet, World world) {
        // Remove the bullet
        world.removeEntity(bullet);
        
        // Try to split the asteroid
        ServiceLoader.load(IAsteroidSplitter.class).stream()
            .map(ServiceLoader.Provider::get)
            .findFirst()
            .ifPresent(splitter -> splitter.createSplitAsteroid(asteroid, world));
        
        // Remove the original asteroid
        world.removeEntity(asteroid);
    }

    private boolean collides(Entity entity1, Entity entity2) {
        float dx = (float) entity1.getX() - (float) entity2.getX();
        float dy = (float) entity1.getY() - (float) entity2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (entity1.getRadius() + entity2.getRadius());
    }
}

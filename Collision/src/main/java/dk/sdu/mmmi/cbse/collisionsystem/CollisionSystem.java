package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.asteroid.AsteroidSplitterImpl;
import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.AsteroidSize;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.HasHealth;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.util.ScoreClient;

public class CollisionSystem implements IPostEntityProcessingService {

    private final IAsteroidSplitter asteroidSplitter;

    public CollisionSystem() {
        this.asteroidSplitter = new AsteroidSplitterImpl();
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity1 : world.getEntities()) {
            for (Entity entity2 : world.getEntities()) {
                if (entity1.getID().equals(entity2.getID())) continue;
                if (entity1 instanceof Asteroid && entity2 instanceof Asteroid) continue;
                if (!collides(entity1, entity2)) continue;

                if (isPair(entity1, entity2, Asteroid.class, Bullet.class)) {
                    Asteroid a = getInstance(entity1, entity2, Asteroid.class);
                    Bullet b = getInstance(entity1, entity2, Bullet.class);
                    handleAsteroidBulletCollision(a, b, world);
                } else if (isPair(entity1, entity2, HasHealth.class, Bullet.class)) {
                    HasHealth target = getInstance(entity1, entity2, HasHealth.class);
                    Bullet bullet = getInstance(entity1, entity2, Bullet.class);
                    handleGenericBulletCollision(target, bullet, world);
                } else if (isPair(entity1, entity2, HasHealth.class, Asteroid.class)) {
                    HasHealth target = getInstance(entity1, entity2, HasHealth.class);
                    Asteroid asteroid = getInstance(entity1, entity2, Asteroid.class);
                    handleGenericAsteroidCollision(target, asteroid, world);
                }
            }
        }
    }

    private boolean isPair(Entity e1, Entity e2, Class<?> clsA, Class<?> clsB) {
        return (clsA.isInstance(e1) && clsB.isInstance(e2)) ||
                (clsA.isInstance(e2) && clsB.isInstance(e1));
    }

    private <T> T getInstance(Entity e1, Entity e2, Class<T> cls) {
        return cls.cast(cls.isInstance(e1) ? e1 : e2);
    }

    private void handleGenericBulletCollision(HasHealth target, Bullet bullet, World world) {
        target.setHealth(target.getHealth() - 1);
        world.removeEntity(bullet);
        if (target.getHealth() <= 0) {
            world.removeEntity((Entity) target);
        }
    }

    private void handleGenericAsteroidCollision(HasHealth target, Asteroid asteroid, World world) {
        target.setHealth(0);
        world.removeEntity(asteroid);

        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                world.removeEntity((Entity) target);
            }
        }, 100);
    }

    private void handleAsteroidBulletCollision(Asteroid asteroid, Bullet bullet, World world) {
        if (bullet.isPlayerBullet()) {
            int points = switch (asteroid.getSize()) {
                case LARGE -> 50;
                case MEDIUM -> 25;
                case SMALL -> 10;
            };
            try {
                ScoreClient.addPoints(points);
            } catch (Exception e) {
                System.err.println("Failed to add points: " + e.getMessage());
            }
        }

        world.removeEntity(bullet);

        if (asteroid.getSize() != AsteroidSize.SMALL) {
            asteroidSplitter.createSplitAsteroid(asteroid, world);
        }

        world.removeEntity(asteroid);
    }

    public Boolean collides(Entity entity1, Entity entity2) {
        float dx = (float) entity1.getX() - (float) entity2.getX();
        float dy = (float) entity1.getY() - (float) entity2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (entity1.getRadius() + entity2.getRadius());
    }
}

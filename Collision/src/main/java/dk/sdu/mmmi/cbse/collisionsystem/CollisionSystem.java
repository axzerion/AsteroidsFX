package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.asteroid.AsteroidSplitterImpl;
import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.playersystem.Player;
import dk.sdu.mmmi.cbse.common.asteroids.AsteroidSize;
import dk.sdu.mmmi.cbse.enemysystem.Enemy;

public class CollisionSystem implements IPostEntityProcessingService {

    private final IAsteroidSplitter asteroidSplitter;

    public CollisionSystem() {
        this.asteroidSplitter = new AsteroidSplitterImpl();
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity1 : world.getEntities()) {
            for (Entity entity2 : world.getEntities()) {
                if (entity1.getID().equals(entity2.getID())) {
                    continue;
                }

                if (entity1 instanceof Asteroid && entity2 instanceof Asteroid) {
                    continue;
                }

                if (!collides(entity1, entity2)) {
                    continue;
                }

                // Normalize and dispatch
                if (isPair(entity1, entity2, Asteroid.class, Bullet.class)) {
                    Asteroid a = (Asteroid) getInstance(entity1, entity2, Asteroid.class);
                    Bullet b = (Bullet) getInstance(entity1, entity2, Bullet.class);
                    handleAsteroidBulletCollision(a, b, world);
                } else if (isPair(entity1, entity2, Player.class, Asteroid.class)) {
                    Player p = (Player) getInstance(entity1, entity2, Player.class);
                    Asteroid a = (Asteroid) getInstance(entity1, entity2, Asteroid.class);
                    handlePlayerAsteroidCollision(p, a, world);
                } else if (isPair(entity1, entity2, Player.class, Bullet.class)) {
                    Player p = (Player) getInstance(entity1, entity2, Player.class);
                    Bullet b = (Bullet) getInstance(entity1, entity2, Bullet.class);
                    handlePlayerBulletCollision(p, b, world);
                } else if (isPair(entity1, entity2, Enemy.class, Bullet.class)) {
                    Enemy e = (Enemy) getInstance(entity1, entity2, Enemy.class);
                    Bullet b = (Bullet) getInstance(entity1, entity2, Bullet.class);
                    handleEnemyBulletCollision(e, b, world);
                } else if (isPair(entity1, entity2, Enemy.class, Asteroid.class)) {
                    Enemy e = (Enemy) getInstance(entity1, entity2, Enemy.class);
                    Asteroid a = (Asteroid) getInstance(entity1, entity2, Asteroid.class);
                    handleEnemyAsteroidCollision(e, a, world);
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

    private void handleEnemyBulletCollision(Enemy enemy, Bullet bullet, World world) {
        enemy.setHealth(enemy.getHealth() - 1);
        world.removeEntity(bullet);
        if (enemy.getHealth() <= 0) {
            world.removeEntity(enemy);
        }
    }

    private void handlePlayerBulletCollision(Player player, Bullet bullet, World world) {
        player.setHealth(player.getHealth() - 1);
        world.removeEntity(bullet);
        if (player.getHealth() <= 0) {
            world.removeEntity(player);
        }
    }

    private void handleAsteroidBulletCollision(Asteroid asteroid, Bullet bullet, World world) {
        world.removeEntity(bullet);

        if (asteroid.getSize() != AsteroidSize.SMALL) {
            asteroidSplitter.createSplitAsteroid(asteroid, world);
        }

        world.removeEntity(asteroid);
    }
    
    private void handlePlayerAsteroidCollision(Player player, Asteroid asteroid, World world) {

        player.setHealth(0);

        world.removeEntity(asteroid);

        new java.util.Timer().schedule(
        new java.util.TimerTask() {
            @Override
            public void run() {
                world.removeEntity(player);
            }
        },
        500
    );
    }

    private void handleEnemyAsteroidCollision(Enemy enemy, Asteroid asteroid, World world) {

        enemy.setHealth(0);
        
        world.removeEntity(asteroid);

        new java.util.Timer().schedule(
        new java.util.TimerTask() {
            @Override
            public void run() {
                world.removeEntity(enemy);
            }
        },
        500
    );
    }

    public Boolean collides(Entity entity1, Entity entity2) {
        float dx = (float) entity1.getX() - (float) entity2.getX();
        float dy = (float) entity1.getY() - (float) entity2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (entity1.getRadius() + entity2.getRadius());
    }

}

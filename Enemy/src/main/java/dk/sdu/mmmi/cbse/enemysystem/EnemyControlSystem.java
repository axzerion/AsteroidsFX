package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.Random;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class EnemyControlSystem implements IEntityProcessingService {

    private Random random = new Random();
    private long lastShotTime = 0;
    private long lastDirectionChange = 0;
    private static final long DIRECTION_CHANGE_INTERVAL = 1000;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity enemy : world.getEntities(Enemy.class)) {
            long currentTime = System.currentTimeMillis();
            
            // Only change direction if enough time has passed
            if (currentTime - lastDirectionChange >= DIRECTION_CHANGE_INTERVAL) {
                // Random turn between -90 and 90 degrees
                enemy.setRotation(enemy.getRotation() + (random.nextInt(181) - 90));
                lastDirectionChange = currentTime;
            }

            // Move with consistent speed
            double speed = 1.0;
            double changeX = Math.cos(Math.toRadians(enemy.getRotation())) * speed;
            double changeY = Math.sin(Math.toRadians(enemy.getRotation())) * speed;

            enemy.setX(enemy.getX() + changeX);
            enemy.setY(enemy.getY() + changeY);

            // Shooting logic
            if (random.nextInt(100) < 10) {
                if (currentTime - lastShotTime >= 100) {
                    ServiceLoader.load(BulletSPI.class).stream()
                            .map(ServiceLoader.Provider::get)
                            .findFirst()
                            .ifPresent(spi -> {
                                Entity bullet = spi.createBullet(enemy, gameData);
                                ((Bullet)bullet).setPlayerBullet(false);
                                world.addEntity(bullet);
                            });
                    lastShotTime = currentTime;
                }
            }

            // Screen boundaries
            if (enemy.getX() < 0) {
                enemy.setX(1);
                enemy.setRotation(0); 
                lastDirectionChange = currentTime; 
            }
            if (enemy.getX() > gameData.getDisplayWidth()) {
                enemy.setX(gameData.getDisplayWidth()-1);
                enemy.setRotation(180);
                lastDirectionChange = currentTime;
            }
            if (enemy.getY() < 0) {
                enemy.setY(1);
            }
            if (enemy.getY() > gameData.getDisplayHeight()) {
                enemy.setY(gameData.getDisplayHeight()-1);
            }
        }

    }
    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}

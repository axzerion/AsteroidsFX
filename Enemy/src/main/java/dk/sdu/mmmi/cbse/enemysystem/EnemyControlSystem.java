package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class EnemyControlSystem implements IEntityProcessingService {
    private static final long SHOOT_COOLDOWN = 200; // 500ms between shots
    private long lastShotTime = 0;

    @Override
    public void process(GameData gameData, World world) {
        long currentTime = System.currentTimeMillis();

        for (Entity player : world.getEntities(Enemy.class)) {
            double random = Math.random();

            if (random < 0.3) {
                player.setRotation(player.getRotation() - 5);
            } else if (random < 0.6) {
                player.setRotation(player.getRotation() + 5);
            }

            if (random < 0.7) {
                double changeX = Math.cos(Math.toRadians(player.getRotation())) * 2;
                double changeY = Math.sin(Math.toRadians(player.getRotation())) * 2;
                player.setX(player.getX() + changeX);
                player.setY(player.getY() + changeY);
            }
            // Check if enough time has passed since the last shot
            if (currentTime - lastShotTime >= SHOOT_COOLDOWN) {
                getBulletSPIs().stream().findFirst().ifPresent(
                        spi -> {world.addEntity(spi.createBullet(player, gameData));}
                );
                lastShotTime = currentTime;
            }

            if (player.getX() < 0) {
                player.setX(1);
            }
            if (player.getX() > gameData.getDisplayWidth()) {
                player.setX(gameData.getDisplayWidth()-1);
            }
            if (player.getY() < 0) {
                player.setY(1);
            }
            if (player.getY() > gameData.getDisplayHeight()) {
                player.setY(gameData.getDisplayHeight()-1);
            }
        }
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}

package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class PlayerControlSystem implements IEntityProcessingService {

    private long lastShotTime = 0;
    private final Collection<BulletSPI> bulletSPIs;

    public PlayerControlSystem() {
        this.bulletSPIs = ServiceLoader.load(BulletSPI.class).stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());
    }

    //for testing
    public PlayerControlSystem(Collection<BulletSPI> bulletSPIs) {
        this.bulletSPIs = bulletSPIs;
    }

    @Override
    public void process(GameData gameData, World world) {

        for (Entity player : world.getEntities(Player.class)) {
            if (gameData.getKeys().isDown(GameKeys.LEFT)) {
                player.setRotation(player.getRotation() - 5);
            }
            if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
                player.setRotation(player.getRotation() + 5);
            }
            if (gameData.getKeys().isDown(GameKeys.UP)) {
                double changeX = Math.cos(Math.toRadians(player.getRotation()));
                double changeY = Math.sin(Math.toRadians(player.getRotation()));
                player.setX(player.getX() + changeX);
                player.setY(player.getY() + changeY);
            }

            if (gameData.getKeys().isDown(GameKeys.SPACE)) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastShotTime >= 100) {
                    bulletSPIs.stream().findFirst().ifPresent(
                            spi -> {
                                Entity bullet = spi.createBullet(player, gameData);
                                ((Bullet) bullet).setPlayerBullet(true);
                                world.addEntity(bullet);
                            }
                    );
                    lastShotTime = currentTime;
                }
            }

            if (player.getX() < 0) {
                player.setX(1);
            }

            if (player.getX() > gameData.getDisplayWidth()) {
                player.setX(gameData.getDisplayWidth() - 1);
            }

            if (player.getY() < 0) {
                player.setY(1);
            }

            if (player.getY() > gameData.getDisplayHeight()) {
                player.setY(gameData.getDisplayHeight() - 1);
            }
        }
    }
}

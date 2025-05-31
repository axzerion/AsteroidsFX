package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.List;
import java.util.Random;
import java.util.Collection;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


public class EnemyControlSystem implements IEntityProcessingService {

    private Random random = new Random();

    private long lastShotTime = 0;


    @Override
    public void process(GameData gameData, World world) {

        for (Entity enemy : world.getEntities(Enemy.class)) {

            double changeX = Math.cos(Math.toRadians(enemy.getRotation()));
            double changeY = Math.sin(Math.toRadians(enemy.getRotation()));

            enemy.setX(enemy.getX() + changeX);
            enemy.setY(enemy.getY() + changeY);

            if (random.nextInt(100) < 20) {
                enemy.setRotation(enemy.getRotation() + (random.nextInt(3)));
            }

            if (random.nextInt(100) < 10) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastShotTime >= 100) {
                    getBulletSPIs().stream().findFirst().ifPresent(
                            spi -> {
                                Entity bullet = spi.createBullet(enemy, gameData);
                                ((Bullet)bullet).setPlayerBullet(false);
                                world.addEntity(bullet);
                            }
                    );
                    lastShotTime = currentTime;
                }
            }

            if (enemy.getX() < 0) {
                enemy.setX(1);
            }

            if (enemy.getX() > gameData.getDisplayWidth()) {
                enemy.setX(gameData.getDisplayWidth()-1);
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
        Module thisModule = this.getClass().getModule();
        ModuleLayer layer = thisModule.getLayer();

        if (layer != null) {
            return ServiceLoader.load(layer, BulletSPI.class)
                    .stream()
                    .map(ServiceLoader.Provider::get)
                    .collect(Collectors.toList());
        } else {
            return List.of(); // fallback: empty if somehow no layer
        }
    }
}

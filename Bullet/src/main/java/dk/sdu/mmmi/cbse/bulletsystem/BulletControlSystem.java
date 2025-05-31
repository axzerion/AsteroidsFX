package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class BulletControlSystem implements IEntityProcessingService, BulletSPI {

    @Override
    public void process(GameData gameData, World world) {

        for (Entity bullet : world.getEntities(Bullet.class)) {
        
            double changeX = Math.cos(Math.toRadians(bullet.getRotation()));
            double changeY = Math.sin(Math.toRadians(bullet.getRotation()));

            bullet.setX(bullet.getX() + changeX * 3);
            bullet.setY(bullet.getY() + changeY * 3);

            // Removes bullets if leaving the screen to not cause lag
            if (bullet.getX() < 0 || 
            bullet.getX() > gameData.getDisplayWidth() ||
            bullet.getY() < 0 || 
            bullet.getY() > gameData.getDisplayHeight()) 
            {
            world.removeEntity(bullet);
        }
    }
}

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Entity bullet = new Bullet();

        bullet.setPolygonCoordinates(2, -2, 2, 2, -2, 2, -2, -2);

        double[] direction = getUnitVector(shooter.getRotation());

        double spawnDistance = 10;
        bullet.setX(shooter.getX() + direction[0] * spawnDistance);
        bullet.setY(shooter.getY() + direction[1] * spawnDistance);

        bullet.setRotation(shooter.getRotation());
        bullet.setRadius(1);

        return bullet;
    }

        // Returns unit vector [cos(θ), sin(θ)] from degrees
        private double[] getUnitVector ( double degrees){
            double radians = Math.toRadians(degrees);
            return new double[]{Math.cos(radians), Math.sin(radians)};
    }
}


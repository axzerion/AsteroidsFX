package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.Random;

public class EnemyPlugin implements IGamePluginService {

    private Entity enemy;

    Random random = new Random();

    public EnemyPlugin() {
    }

    @Override
    public void start(GameData gameData, World world) {

        // Add enemy ship to the world
        enemy = createEnemyShip(gameData);
        world.addEntity(enemy);
    }

    private Entity createEnemyShip(GameData gameData) {

        Entity enemyShip = new Enemy();

        enemyShip.setPolygonCoordinates(-10,-10,20,0,-10,10);
        
        // Set fixed spawn position
        enemyShip.setX(300);
        enemyShip.setY(gameData.getDisplayHeight()/2);
        
        enemyShip.setRadius(8);
        return enemyShip;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        world.removeEntity(enemy);
    }

}

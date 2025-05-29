package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.Random;

public class EnemyPlugin implements IGamePluginService {
    private Entity enemy;
    private Random random = new Random();
    
    @Override
    public void start(GameData gameData, World world) {
        enemy = createEnemyShip(gameData);
        world.addEntity(enemy);
    }
    
    private Entity createEnemyShip(GameData gameData) {
        Entity enemyShip = new Enemy();

        enemyShip.setPolygonCoordinates(-5,-5,10,0,-5,5); // size of enemy ship

        // Set color to red
        enemyShip.setColor("red");

        // Spawn at random position
        enemyShip.setX(random.nextDouble() * gameData.getDisplayWidth());
        enemyShip.setY(random.nextDouble() * gameData.getDisplayHeight());
        enemyShip.setRadius(8);

        return enemyShip;
    }
    
    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(enemy);
    }
} 
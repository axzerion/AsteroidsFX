package dk.sdu.mmmi.cbse.gameplugins;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.enemysystem.Enemy;
import java.util.Random;

public class GamePlugin implements IGamePluginService {

    private Entity enemy;
    Random random = new Random();

    public GamePlugin() {
        System.out.println("Enemy GamePlugin constructed!");
    }

    @Override
    public void start(GameData gameData, World world) {
        System.out.println("Enemy GamePlugin.start() called!"); // This must print
        enemy = createEnemyShip(gameData);
        world.addEntity(enemy);
        System.out.println("Enemy ship added to world: " + enemy);
    }

    private Entity createEnemyShip(GameData gameData) {
        Entity enemyShip = new Enemy();
        enemyShip.setPolygonCoordinates(-10,-10,20,0,-10,10);
        enemyShip.setX(300);
        enemyShip.setY(gameData.getDisplayHeight()/2);
        enemyShip.setRadius(8);
        return enemyShip;
    }

    @Override
    public void stop(GameData gameData, World world) {
        System.out.println("Enemy GamePlugin.stop() called!");
        world.removeEntity(enemy);
    }
}

package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Game extends Application {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();
    private final List<IGamePluginService> gamePluginServices;
    private final List<IEntityProcessingService> entityProcessingServiceList;
    private final List<IPostEntityProcessingService> postEntityProcessingServices;

    public Game(List<IGamePluginService> gamePluginServices,
                List<IEntityProcessingService> entityProcessingServiceList,
                List<IPostEntityProcessingService> postEntityProcessingServices) {
        this.gamePluginServices = gamePluginServices;
        this.entityProcessingServiceList = entityProcessingServiceList;
        this.postEntityProcessingServices = postEntityProcessingServices;
    }

    @Override
    public void start(Stage window) {
        System.out.println("Starting game with " + gamePluginServices.size() + " plugins");

        Text text = new Text(10, 20, "Score: 0");
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.getChildren().add(text);

        Text playerHealthText = new Text(10, 40, "Player Health: 5");
        Text enemyHealthText = new Text(gameData.getDisplayWidth() - 150, 40, "Enemy Health: 5");
        gameWindow.getChildren().add(playerHealthText);
        gameWindow.getChildren().add(enemyHealthText);

        Scene scene = getScene();

        for (IGamePluginService iGamePlugin : getGamePluginServices()) {
            System.out.println("Starting plugin: " + iGamePlugin.getClass().getName());
            iGamePlugin.start(gameData, world);
        }

        System.out.println("Entities after plugin start: " + world.getEntities().size());

        for (Entity entity : world.getEntities()) {
            Polygon polygon = new Polygon(entity.getPolygonCoordinates());
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);
        }

        window.setScene(scene);
        window.setTitle("ASTEROIDS");
        window.show();
    }

    private Scene getScene() {
        Scene scene = new Scene(gameWindow);
        scene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) gameData.getKeys().setKey(GameKeys.LEFT, true);
            if (event.getCode().equals(KeyCode.RIGHT)) gameData.getKeys().setKey(GameKeys.RIGHT, true);
            if (event.getCode().equals(KeyCode.UP)) gameData.getKeys().setKey(GameKeys.UP, true);
            if (event.getCode().equals(KeyCode.SPACE)) gameData.getKeys().setKey(GameKeys.SPACE, true);
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) gameData.getKeys().setKey(GameKeys.LEFT, false);
            if (event.getCode().equals(KeyCode.RIGHT)) gameData.getKeys().setKey(GameKeys.RIGHT, false);
            if (event.getCode().equals(KeyCode.UP)) gameData.getKeys().setKey(GameKeys.UP, false);
            if (event.getCode().equals(KeyCode.SPACE)) gameData.getKeys().setKey(GameKeys.SPACE, false);
        });
        return scene;
    }

    public void render() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
                gameData.getKeys().update();
            }
        }.start();
    }

    private void update() {
        for (IEntityProcessingService service : getEntityProcessingServices()) {
            service.process(gameData, world);
        }
        for (IPostEntityProcessingService service : getPostEntityProcessingServices()) {
            service.process(gameData, world);
        }
    }

    private void draw() {
        for (Entity entity : polygons.keySet()) {
            if (!world.getEntities().contains(entity)) {
                Polygon removedPolygon = polygons.remove(entity);
                gameWindow.getChildren().remove(removedPolygon);
            }
        }

        for (Entity entity : world.getEntities()) {
            Polygon polygon = polygons.computeIfAbsent(entity,
                    e -> {
                        Polygon p = new Polygon(e.getPolygonCoordinates());
                        gameWindow.getChildren().add(p);
                        return p;
                    });
            polygon.setTranslateX(entity.getX());
            polygon.setTranslateY(entity.getY());
            polygon.setRotate(entity.getRotation());

            String className = entity.getClass().getName();
            if (className.equals("dk.sdu.mmmi.cbse.enemysystem.Enemy")) {
                polygon.setFill(javafx.scene.paint.Color.RED);
            } else if (className.equals("dk.sdu.mmmi.cbse.playersystem.Player")) {
                polygon.setFill(javafx.scene.paint.Color.BLACK);
            }

            for (javafx.scene.Node node : gameWindow.getChildren()) {
                if (node instanceof Text textNode) {
                    if (className.equals("dk.sdu.mmmi.cbse.playersystem.Player") && textNode.getText().startsWith("Player Health:")) {
                        textNode.setText("Player Health: " + getFieldValue(entity, "health"));
                    } else if (className.equals("dk.sdu.mmmi.cbse.enemysystem.Enemy") && textNode.getText().startsWith("Enemy Health:")) {
                        textNode.setText("Enemy Health: " + getFieldValue(entity, "health"));
                    } else if (textNode.getText().startsWith("Score:")) {
                        textNode.setText("Score: " + dk.sdu.mmmi.cbse.common.data.Score.getPoints());
                    }
                }
            }
        }
    }

    private int getFieldValue(Entity entity, String fieldName) {
        try {
            var field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getInt(entity);
        } catch (Exception e) {
            return -1;
        }
    }

    public List<IGamePluginService> getGamePluginServices() {
        return gamePluginServices;
    }

    public List<IEntityProcessingService> getEntityProcessingServices() {
        return entityProcessingServiceList;
    }

    public List<IPostEntityProcessingService> getPostEntityProcessingServices() {
        return postEntityProcessingServices;
    }

    @Override
    public void stop() {
        gamePluginServices.forEach(plugin -> plugin.stop(gameData, world));
    }
}

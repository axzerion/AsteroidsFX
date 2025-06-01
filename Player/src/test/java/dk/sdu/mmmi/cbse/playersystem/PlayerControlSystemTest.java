package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerControlSystemTest {

    private GameData gameData;
    private World world;
    private PlayerControlSystem playerControlSystem;
    private Player player;

    @BeforeEach
    void setup() {
        gameData = new GameData();
        gameData.setDisplayWidth(800);
        gameData.setDisplayHeight(600);
        world = new World();

        // Mock BulletSPI
        BulletSPI mockSPI = (shooter, gd) -> {
            Bullet bullet = new Bullet();
            bullet.setX(shooter.getX());
            bullet.setY(shooter.getY());
            bullet.setRotation(shooter.getRotation());
            return bullet;
        };

        playerControlSystem = new PlayerControlSystem(List.of(mockSPI));

        player = new Player();
        player.setX(100);
        player.setY(100);
        player.setRotation(0);
        world.addEntity(player);
    }

    @Nested
    class MovementTests {
        @Test
        void testPlayerMovesForward() {
            gameData.getKeys().setKey(GameKeys.UP, true);

            playerControlSystem.process(gameData, world);

            assertTrue(player.getX() > 100, "Player X should increase when moving forward");
            assertEquals(100, player.getY(), "Player Y should not change when moving straight right");
        }

        @Test
        void testPlayerTurnsLeft() {
            gameData.getKeys().setKey(GameKeys.LEFT, true);

            playerControlSystem.process(gameData, world);

            assertEquals(-5, player.getRotation(), "Player should rotate left by 5 degrees");
        }

        @Test
        void testPlayerTurnsRight() {
            gameData.getKeys().setKey(GameKeys.RIGHT, true);

            playerControlSystem.process(gameData, world);

            assertEquals(5, player.getRotation(), "Player should rotate right by 5 degrees");
        }

        @Test
        void testPlayerMovesDiagonally() {
            gameData.getKeys().setKey(GameKeys.UP, true);
            gameData.getKeys().setKey(GameKeys.RIGHT, true);

            playerControlSystem.process(gameData, world);

            assertTrue(player.getX() > 100, "Player X should increase when moving diagonally");
            assertTrue(player.getY() > 100, "Player Y should increase when moving diagonally");
        }
    }

    @Nested
    class ShootingTests {
        @Test
        void testPlayerShoots() {
            gameData.getKeys().setKey(GameKeys.SPACE, true);

            playerControlSystem.process(gameData, world);

            assertTrue(world.getEntities().stream().anyMatch(e -> e instanceof Bullet),
                    "A bullet should be created when shooting");
        }

        @Test
        void testShootingCooldown() {
            gameData.getKeys().setKey(GameKeys.SPACE, true);

            playerControlSystem.process(gameData, world);
            int initialBulletCount = (int) world.getEntities().stream()
                    .filter(e -> e instanceof Bullet)
                    .count();

            // Immediately shoot again (should not add another bullet)
            playerControlSystem.process(gameData, world);
            int secondBulletCount = (int) world.getEntities().stream()
                    .filter(e -> e instanceof Bullet)
                    .count();

            assertEquals(initialBulletCount, secondBulletCount,
                    "No new bullet should be created during cooldown period");
        }
    }
}

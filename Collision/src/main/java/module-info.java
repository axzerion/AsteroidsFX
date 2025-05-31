import dk.sdu.mmmi.cbse.collisionsystem.CollisionSystem;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

module Collision {
    requires Common;
    requires CommonBullet;
    requires Player;
    requires CommonAsteroids;
    requires Asteroids;
    requires Enemy;
    provides IPostEntityProcessingService with CollisionSystem;
}
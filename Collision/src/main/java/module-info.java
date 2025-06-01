import dk.sdu.mmmi.cbse.collisionsystem.CollisionSystem;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

module Collision {
    requires Common;
    requires CommonBullet;
    requires CommonAsteroids;

    provides IPostEntityProcessingService with CollisionSystem;
    uses dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
}
module Core {
    requires Common;
    requires javafx.graphics;
    requires Player;
    requires Enemy;
    requires Asteroids;
    requires Collision;
    requires CommonBullet;
    requires CommonAsteroids;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    opens dk.sdu.mmmi.cbse.main to spring.core;
    exports dk.sdu.mmmi.cbse.main;
    uses dk.sdu.mmmi.cbse.common.services.IGamePluginService;
    uses dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
}



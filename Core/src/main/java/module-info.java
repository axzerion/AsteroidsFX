module Core {

    requires Common;
    requires CommonBullet;
    requires CommonAsteroids;
    requires javafx.controls;
    requires javafx.graphics;
    requires Asteroids;
    requires Collision;
    requires Player;
    requires Enemy;

    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.web;

    opens dk.sdu.mmmi.cbse.main to spring.core, spring.beans, spring.context;

    uses dk.sdu.mmmi.cbse.common.services.IGamePluginService;
    uses dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

    exports dk.sdu.mmmi.cbse.main;
}

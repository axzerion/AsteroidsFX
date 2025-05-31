/**
 *
 */
module core {

    requires Common;
    requires javafx.controls;
    requires javafx.graphics;
    requires Asteroids;
    requires Collision;

    requires CommonAsteroids;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires Player;
    requires enemysystem;

    opens dk.sdu.mmmi.cbse.main to spring.core, spring.beans, spring.context;
    exports dk.sdu.mmmi.cbse.main;

    uses dk.sdu.mmmi.cbse.common.services.IGamePluginService;
    uses dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
}

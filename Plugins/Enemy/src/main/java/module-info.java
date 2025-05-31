
module enemysystem {
    requires Common;
    requires CommonBullet;

    exports dk.sdu.mmmi.cbse.enemysystem;
    exports dk.sdu.mmmi.cbse.gameplugins;

    provides dk.sdu.mmmi.cbse.common.services.IGamePluginService
            with dk.sdu.mmmi.cbse.gameplugins.GamePlugin;

    provides dk.sdu.mmmi.cbse.common.services.IEntityProcessingService
            with dk.sdu.mmmi.cbse.enemysystem.EnemyControlSystem;
}
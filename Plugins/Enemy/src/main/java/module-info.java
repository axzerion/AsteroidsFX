module Enemy {
    requires Common;
    requires CommonBullet;
    requires Bullet;

    provides dk.sdu.mmmi.cbse.common.services.IGamePluginService
            with dk.sdu.mmmi.cbse.enemysystem.EnemyPlugin;
    provides dk.sdu.mmmi.cbse.common.services.IEntityProcessingService
            with dk.sdu.mmmi.cbse.enemysystem.EnemyControlSystem;

    exports dk.sdu.mmmi.cbse.enemysystem;
} 
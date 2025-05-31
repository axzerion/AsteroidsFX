package dk.sdu.mmmi.cbse.common.bullet;

import dk.sdu.mmmi.cbse.common.data.Entity;


public class Bullet extends Entity {
    private boolean isPlayerBullet;

    public boolean isPlayerBullet() {
        return isPlayerBullet;
    }

    public void setPlayerBullet(boolean playerBullet) {
        isPlayerBullet = playerBullet;
    }
}


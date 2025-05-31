package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.IHealth;

public class Enemy extends Entity implements IHealth {
    private int health = 5;  // Start with 5 health

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    public void takeDamage() {
        this.health--;
    }
}

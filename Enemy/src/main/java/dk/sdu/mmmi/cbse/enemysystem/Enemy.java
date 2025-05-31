package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;


public class Enemy extends Entity {
    private int health = 5;  // Start with 5 health

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void takeDamage() {
        this.health--;
    }
}

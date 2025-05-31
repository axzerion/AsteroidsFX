package dk.sdu.mmmi.cbse.common.asteroids;

import dk.sdu.mmmi.cbse.common.data.Entity;


public class Asteroid extends Entity {
    private AsteroidSize size;

    public AsteroidSize getSize() {
        return size;
    }

    public void setSize(AsteroidSize size) {
        this.size = size;
    }
}

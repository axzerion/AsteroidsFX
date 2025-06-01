package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Service interface for processing game entities during the main game loop.
 * This interface is responsible for updating the state of game entities,
 * such as movement, collisionsystem detection, and other game logic.
 */
public interface  IEntityProcessingService {

    /**
     * Processes all relevant entities in the game world.
     * This method is called once per game frame to update entity states.
     */
    void process(GameData gameData, World world);
}

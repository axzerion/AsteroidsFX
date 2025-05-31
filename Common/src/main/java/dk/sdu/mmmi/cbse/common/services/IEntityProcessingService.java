package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Service interface for processing game entities during the main game loop.
 * This interface is responsible for updating the state of game entities,
 * such as movement, collisionsystem detection, and other game logic.
 */
public interface IEntityProcessingService {

    /**
     * Processes all relevant entities in the game world.
     * This method is called once per game frame to update entity states.
     * 
     * Pre-conditions:
     * - gameData must not be null
     * - world must not be null
     * - The game must be in a running state
     * 
     * Post-conditions:
     * - All relevant entities have been updated according to game rules
     * - Entity states reflect the changes made during processing
     * - The world state is consistent with the processing results
     *
     * @param gameData The game data containing current game state and configuration
     * @param world The game world containing entities to be processed
     */
    void process(GameData gameData, World world);
}

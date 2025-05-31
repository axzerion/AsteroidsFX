package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Service interface for post-processing game entities after the main entity processing phase.
 * This interface is responsible for handling operations that should occur after all
 * entities have been processed, such as cleanup, state validation, or final adjustments.
 */
public interface IPostEntityProcessingService {

    /**
     * Performs post-processing operations on the game world.
     * This method is called after all entity processing is complete.
     * 
     * Pre-conditions:
     * - gameData must not be null
     * - world must not be null
     * - All entity processing services have completed their operations
     * - The game must be in a running state
     * 
     * Post-conditions:
     * - Any necessary post-processing operations have been completed
     * - The game world is in a consistent state
     * - All cleanup operations have been performed
     *
     * @param gameData The game data containing current game state and configuration
     * @param world The game world to be post-processed
     */
    void process(GameData gameData, World world);

}

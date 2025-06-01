package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Service interface for game plugins that manage the lifecycle of game entities.
 * This interface is responsible for starting and stopping game plugins, which typically
 * handle the creation and cleanup of game entities.
 */
public interface IGamePluginService {

    /**
     * Initializes and starts the game plugin.
     * 
     * Pre-conditions:
     * - gameData must not be null
     * - world must not be null
     * - The game must be in a state where new entities can be added
     * 
     * Post-conditions:
     * - The plugin is initialized and ready to operate
     * - Any necessary game entities are created and added to the world
     * - The plugin's state is properly initialized
     *
     * @param gameData The game data containing current game state and configuration
     * @param world The game world where entities can be added
     */
    void start(GameData gameData, World world);

    /**
     * Stops and cleans up the game plugin.
     */

    void stop(GameData gameData, World world);

}

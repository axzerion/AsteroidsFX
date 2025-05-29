package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
    * Interface for game plugins that manage entity lifecycle
    * @pre gameData != null && world != null
    * @post world contains new entities after start()
    * @post world does not contain plugin's entities after stop()
    */
    public interface IGamePluginService {
        /**
         * Initializes and adds entities to the game world
         * @pre gameData != null && world != null
         * @post world contains new entities
         */
        void start(GameData gameData, World world);
        
        /**
         * Removes plugin's entities from the game world
         * @pre gameData != null && world != null
         * @post world does not contain plugin's entities
         */
        void stop(GameData gameData, World world);
    }

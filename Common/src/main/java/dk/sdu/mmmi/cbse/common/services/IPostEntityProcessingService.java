package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Service interface for post-processing game entities after the main entity processing phase.
 * This interface is responsible for handling operations that should occur after all
 * entities have been processed, such as cleanup, state validation, or final adjustments.
 */
public interface IPostEntityProcessingService {

    void process(GameData gameData, World world);

}

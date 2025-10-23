package anchors.rogue.ecs.managers

import ktx.log.logger

/**
 * Manager interface defining setup and teardown methods for system managers.
 * Each manager implementing this interface should provide its own setup and teardown logic.
 * Each manager represents a distinct singleton in the game architecture, responsible for a specific aspect of the game's functionality.
 */
abstract class Manager {
    // Logger instance for logging messages related to the Manager
    open val logger = logger<Manager>()

    /**
     * Initializes the manager, setting up necessary resources or configurations.
     * This method is called when the manager is first created or activated.
     */
    open fun setup() = logger.info { "Running setup..." }

    /**
     * Cleans up resources or configurations used by the manager.
     * This method is called when the manager is no longer needed or is being deactivated.
     */
    open fun teardown() = logger.info { "Running teardown..." }
}

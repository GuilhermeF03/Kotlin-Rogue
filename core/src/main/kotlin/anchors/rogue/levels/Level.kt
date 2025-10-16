package anchors.rogue.levels

import com.github.quillraven.fleks.World

/**
 * Level interface defines the structure for game levels.
 * It includes methods for initializing and disposing of level resources.
 */
interface Level {
    /**
     * Called when the level is initialized.
     * @param world The game world context in which the level is initialized.
     */
    fun setup(world : World)
    /**
     * Called when the level is disposed of.
     * This method should handle any necessary cleanup of resources.
     */
    fun onDispose()
}

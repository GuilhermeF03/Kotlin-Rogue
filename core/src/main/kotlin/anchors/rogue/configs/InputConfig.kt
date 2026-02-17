package anchors.rogue.configs

import canopy.input.InputBind
import com.badlogic.gdx.Input

/**
 * This object stores all default keybinds - can be changed later in-game
 */
object InputConfig {
    fun getInputMappings(): Array<Pair<String, List<InputBind>>> =
        arrayOf(
            "move-left" to
                listOf(
                    InputBind.keyboardBind(Input.Keys.A),
                    InputBind.keyboardBind(Input.Keys.LEFT),
                ),
            "move-right" to
                listOf(
                    InputBind.keyboardBind(Input.Keys.D),
                    InputBind.keyboardBind(Input.Keys.RIGHT),
                ),
            "move-up" to
                listOf(
                    InputBind.keyboardBind(Input.Keys.W),
                    InputBind.keyboardBind(Input.Keys.UP),
                ),
            "move-down" to
                listOf(
                    InputBind.keyboardBind(Input.Keys.S),
                    InputBind.keyboardBind(Input.Keys.DOWN),
                ),
        )
}

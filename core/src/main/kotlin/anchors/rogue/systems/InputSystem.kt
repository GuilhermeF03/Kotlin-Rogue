package anchors.rogue.systems

import anchors.rogue.shared.utils.input.InputEvent
import anchors.rogue.shared.utils.input.InputManager
import anchors.rogue.shared.utils.nodes.core.GlobalNodeSystem
import anchors.rogue.shared.utils.nodes.core.UpdatePhase
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import ktx.app.KtxInputAdapter

/**
 * Low-level input → event dispatcher
 * Works together with InputManager (poll-based actions)
 */
class InputSystem(
    private val input: InputManager,
) : GlobalNodeSystem(UpdatePhase.Input),
    KtxInputAdapter {
    override fun onSystemInit() {
        super.onSystemInit()
        Gdx.input.inputProcessor = this
    }

    override fun beforeProcess(delta: Float) {
        input.update() // Poll keyboard/mouse/gamepad actions
    }

    // ┌────────────────────────────────────┐
    // │ Mouse buttons (pointer == 0 only)  │
    // └────────────────────────────────────┘
    override fun touchDown(
        screenX: Int,
        screenY: Int,
        pointer: Int,
        button: Int,
    ): Boolean {
        if (pointer == 0) {
            val coords = screenToWorld(screenX, screenY)
            dispatch(InputEvent.MouseButton(coords.x.toInt(), coords.y.toInt(), button, true))
            return true
        }
        return false
    }

    override fun touchUp(
        screenX: Int,
        screenY: Int,
        pointer: Int,
        button: Int,
    ): Boolean {
        if (pointer == 0) {
            val coords = screenToWorld(screenX, screenY)
            dispatch(InputEvent.MouseButton(coords.x.toInt(), coords.y.toInt(), button, false))
            return true
        }
        return false
    }

    // ┌────────────────────────────────────┐
    // │ Mouse movement (optional)         │
    // └────────────────────────────────────┘
    override fun mouseMoved(
        screenX: Int,
        screenY: Int,
    ): Boolean {
        val coords = screenToWorld(screenX, screenY)
        dispatch(InputEvent.MouseMove(coords.x.toInt(), coords.y.toInt()))
        return false
    }

    // ┌────────────────────────────────────┐
    // │ Keyboard events                    │
    // └────────────────────────────────────┘
    override fun keyDown(keycode: Int): Boolean {
        dispatch(InputEvent.Key(keycode, true))
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        dispatch(InputEvent.Key(keycode, false))
        return true
    }

    // ┌────────────────────────────────────┐
    // │ Scroll wheel                       │
    // └────────────────────────────────────┘
    override fun scrolled(
        amountX: Float,
        amountY: Float,
    ): Boolean {
        dispatch(InputEvent.Scroll(amountX, amountY))
        return false
    }

    // ┌──────────────────────────────┐
    // │ Dispatch to SceneManager     │
    // └──────────────────────────────┘
    private fun dispatch(event: InputEvent) {
        sceneManager.currScene?.input(event)
    }

    // ┌───────────────────────────────────────────────────────┐
    // │ Screen → world conversion (through current main cam) │
    // └───────────────────────────────────────────────────────┘
    private fun screenToWorld(
        x: Int,
        y: Int,
    ): Vector2 {
        val camera = sceneManager.activeCamera?.camera ?: return Vector2.Zero

        val vec = Vector3(x.toFloat(), y.toFloat(), 0f)
        camera.unproject(vec)

        return Vector2(vec.x, vec.y)
    }
}

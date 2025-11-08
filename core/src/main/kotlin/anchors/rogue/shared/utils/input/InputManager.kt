package anchors.rogue.shared.utils.input

import anchors.rogue.shared.managers.Manager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.ControllerListener
import com.badlogic.gdx.controllers.Controllers

class InputManager :
    Manager,
    ControllerListener {
    private val actionKeys = mutableMapOf<String, MutableList<Int>>()
    private val actionButtons = mutableMapOf<String, MutableList<Int>>()
    private val actionGPButtons = mutableMapOf<String, MutableList<Int>>()
    private val actionGPAxes = mutableMapOf<String, MutableList<Int>>()

    private val actionState = mutableMapOf<String, Boolean>()
    private val actionJustPressed = mutableSetOf<String>()
    private val actionJustReleased = mutableSetOf<String>()

    init {
        Controllers.addListener(this)
    }

    // -------------------
    // Action mapping API
    // -------------------

    fun mapKey(
        action: String,
        key: Int,
    ) {
        actionKeys.getOrPut(action) { mutableListOf() }.add(key)
    }

    fun mapButton(
        action: String,
        btn: Int,
    ) {
        actionButtons.getOrPut(action) { mutableListOf() }.add(btn)
    }

    fun mapGamepadButton(
        action: String,
        btn: Int,
    ) {
        actionGPButtons.getOrPut(action) { mutableListOf() }.add(btn)
    }

    fun mapGamepadAxis(
        action: String,
        axis: Int,
    ) {
        actionGPAxes.getOrPut(action) { mutableListOf() }.add(axis)
    }

    // -------------------
    // Query API (like Godot)
    // -------------------

    fun isActionPressed(action: String): Boolean = actionState[action] == true

    fun isActionJustPressed(action: String): Boolean = actionJustPressed.contains(action)

    fun isActionJustReleased(action: String): Boolean = actionJustReleased.contains(action)

    // -------------------
    // Internal state update
    // -------------------

    fun update() {
        actionJustPressed.clear()
        actionJustReleased.clear()

        actionKeys.forEach { (action, keys) ->
            val pressedNow = keys.any { Gdx.input.isKeyPressed(it) }
            updateState(action, pressedNow)
        }

        actionButtons.forEach { (action, buttons) ->
            val pressedNow = buttons.any { Gdx.input.isButtonPressed(it) }
            updateState(action, pressedNow)
        }

        updateGamepadState()
    }

    private fun updateGamepadState() {
        for (controller in Controllers.getControllers()) {
            actionGPButtons.forEach { (action, bts) ->
                val pressedNow = bts.any { controller.getButton(it) }
                updateState(action, pressedNow)
            }

            actionGPAxes.forEach { (action, axes) ->
                val pressedNow = axes.any { kotlin.math.abs(controller.getAxis(it)) > 0.3f }
                updateState(action, pressedNow)
            }
        }
    }

    private fun updateState(
        action: String,
        newState: Boolean,
    ) {
        val old = actionState[action] ?: false
        actionState[action] = newState
        if (!old && newState) actionJustPressed += action
        if (old && !newState) actionJustReleased += action
    }

    override fun connected(p0: Controller?) = Unit

    override fun disconnected(p0: Controller?) = Unit

    // -------------------
    // Gamepad listener callbacks
    // -------------------

    override fun buttonDown(
        controller: Controller?,
        buttonCode: Int,
    ): Boolean = false

    override fun buttonUp(
        controller: Controller?,
        buttonCode: Int,
    ): Boolean = false

    override fun axisMoved(
        controller: Controller?,
        axisCode: Int,
        value: Float,
    ): Boolean = false
}

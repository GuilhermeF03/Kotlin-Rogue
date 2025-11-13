package anchors.rogue.shared.utils.input

import com.badlogic.gdx.math.Vector2

/**
 * Represents an input event detected by GDX and mapped by the Input Dispatcher
 */
sealed class InputEvent(
    open val action : String,
    open val state : InputState
) {
    /**
     * Whether or not this event was handled, for propagation concerns
     */
    internal var isHandled = false

    fun isActionPressed(action : String) = this.action == action && state == InputState.Pressed
    fun isActionJustPressed(action : String) = this.action == action && state == InputState.JustPressed
    fun isActionReleased(action : String) = this.action == action && state == InputState.Released
    fun isActionJustReleased(action: String) = this.action == action && state == InputState.JustReleased

    fun isAnyActionPressed() = state == InputState.Pressed
    fun isAnyActionJustPressed() = state == InputState.JustPressed
    fun isAnyActionReleased() = state == InputState.Released
    fun isAnyActionJustReleased() = state == InputState.JustReleased
}

enum class InputState{Pressed, Released, JustPressed, JustReleased, Other}

class ButtonInputEvent(
    action : String,
    state: InputState
) : InputEvent(action, state)

class MouseButtonEvent(
    val screenPos : Vector2,
    action : String,
    state : InputState
) : InputEvent(action, state)

class MouseMoveEvent(
    val screenPos : Vector2,
    action : String,
) : InputEvent(action, InputState.Other)

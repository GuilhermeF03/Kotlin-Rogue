package anchors.rogue.shared.utils.input

import com.badlogic.gdx.math.Vector2

/**
 * Represents an input event detected by GDX and mapped by the Input Dispatcher
 */
sealed class InputEvent(
    protected open val actions : List<String>,
    protected open val type : InputType
) {
    /**
     * Whether or not this event was handled, for propagation concerns
     */
    internal var isHandled = false

    fun isActionPressed(action : String) = action in actions && type == InputType.Pressed
    fun isActionReleased(action : String) = action in actions && type == InputType.Released
    fun isActionJustPressed(action : String) = action in actions && type == InputType.JustPressed
}

enum class InputType{Pressed, Released, JustPressed, Other}

class ButtonInputEvent(
    actions : List<String>,
    type : InputType
) : InputEvent(actions, type)

class MouseButtonEvent(
    val coords : Vector2,
    actions : List<String>,
    type : InputType
) : InputEvent(actions, type)

class MouseMoveEvent(
    val coords : Vector2,
    actions : List<String>,
) : InputEvent(actions, InputType.Other)

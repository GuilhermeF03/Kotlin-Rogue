package anchors.rogue.shared.utils.input

/**
 * Represents an input event detected by GDX and mapped by the Input Dispatcher
 */
sealed class InputEvent {
    /**
     * Whether or not this event was handled, for propagation concerns
     */
    var isHandled = false

    data class Key(val keycode: Int, val pressed: Boolean) : InputEvent()
    data class MouseButton(val x: Int, val y: Int, val button: Int, val pressed: Boolean) : InputEvent()
    data class MouseMove(val x: Int, val y: Int) : InputEvent()
    data class Scroll(val amountX: Float, val amountY: Float) : InputEvent()
}

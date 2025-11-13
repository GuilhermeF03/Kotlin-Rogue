package anchors.rogue.shared.utils.input

import anchors.rogue.shared.managers.ManagersRegistry
import anchors.rogue.shared.utils.nodes.SceneManager
import anchors.rogue.shared.utils.nodes.core.GlobalNodeSystem
import anchors.rogue.shared.utils.nodes.core.UpdatePhase

class InputSystem(vararg pairs: Pair<String, List<InputBind>>) :
    GlobalNodeSystem(UpdatePhase.Input)
{
    private val mapper = InputMapper()
    private val actionsState = mutableMapOf<String, InputState>()

    init {
        // Map all input actions at startup
        pairs.forEach { (action, binds) -> mapper.mapAction(action, binds) }
    }

    // ------------------------------------------------------------------------
    // Polling per frame
    // ------------------------------------------------------------------------
    override fun afterProcess(delta: Float) {
        mapper.mappings.forEach { (action, binds) ->
            val isPressed = binds.any { it.isBeingPressed() }
            val prevState = actionsState[action] ?: InputState.Released

            // Compute the next state
            val nextState = getInputState(action, if (isPressed) InputState.Pressed else InputState.Released)

            // Dispatch events
            when (nextState) {
                InputState.JustPressed -> {
                    dispatchEvents(listOf(ButtonInputEvent(action, InputState.JustPressed)))
                }
                InputState.Pressed -> {
                    // Only dispatch if the action was already Pressed (avoid spam for taps)
                    if (prevState == InputState.Pressed) {
                        dispatchEvents(listOf(ButtonInputEvent(action, InputState.Pressed)))
                    }
                }
                InputState.JustReleased -> {
                    dispatchEvents(listOf(ButtonInputEvent(action, InputState.JustReleased)))
                }
                else -> Unit
            }
        }
    }

    // ------------------------------------------------------------------------
    // Compute next state based on previous state and raw input
    // ------------------------------------------------------------------------
    private fun getInputState(action: String, newState: InputState): InputState {
        val prev = actionsState[action] ?: InputState.Released

        val prevIsPressedEvent = prev in listOf(InputState.Pressed, InputState.JustPressed)
        val prevIsReleasedEvent= prev in listOf(InputState.Released, InputState.JustReleased)

        val next = when (newState) {
            InputState.Pressed if (prevIsReleasedEvent) -> InputState.JustPressed
            InputState.Pressed -> InputState.Pressed
            InputState.Released if (prevIsPressedEvent) -> InputState.JustReleased
            else -> InputState.Released
        }

        actionsState[action] = next
        return next
    }

    // ------------------------------------------------------------------------
    // Dispatch events to the SceneManager
    // ------------------------------------------------------------------------
    private fun dispatchEvents(events: List<InputEvent>) {
        val sceneManager = ManagersRegistry.get(SceneManager::class)
        events.forEach { sceneManager.onInput(it) }
    }
}

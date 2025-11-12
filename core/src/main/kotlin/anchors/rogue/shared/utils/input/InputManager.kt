package anchors.rogue.shared.utils.input

import anchors.rogue.shared.managers.Manager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.ControllerListener
import com.badlogic.gdx.controllers.Controllers
import ktx.app.KtxInputAdapter

class InputManager : Manager, KtxInputAdapter
    //ControllerListener
{
    private val mapper = InputMapper()
    private val actionsState = mutableMapOf<String, InputType>()

    init {
        Gdx.input.inputProcessor = this
    }

    override fun keyDown(keycode: Int): Boolean {
        val bind = InputBind.keyboardBind(keycode)
        val actions = mapper.mapToAction(bind)
        val event = ButtonInputEvent()
    }


}

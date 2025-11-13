package anchors.rogue.shared.utils.input

import com.badlogic.gdx.Gdx
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InputBind(private val type : InputBindType, val code : Int){

    /**
     *
     */
    fun isBeingPressed() = when(type){
        InputBindType.KeyBind -> Gdx.input.isKeyPressed(code)
        InputBindType.MouseBind -> Gdx.input.isButtonPressed(code)
    }

    companion object {
        fun keyboardBind(code : Int) = InputBind(InputBindType.KeyBind, code)

        fun mouseBind(code : Int) = InputBind(InputBindType.MouseBind, code)
    }
}
enum class InputBindType{ KeyBind, MouseBind }


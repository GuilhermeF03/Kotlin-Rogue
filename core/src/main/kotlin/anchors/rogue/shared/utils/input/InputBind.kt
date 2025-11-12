package anchors.rogue.shared.utils.input

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class InputBind(private val type : InputBindType, val code : Int){
    companion object{
        fun keyboardBind(code : Int) = InputBind(InputBindType.KeyBind, code)

        fun mouseBind(code : Int) = InputBind(InputBindType.MouseBind, code)
    }
}
enum class InputBindType{ KeyBind, MouseBind }


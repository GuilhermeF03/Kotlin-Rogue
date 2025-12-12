package anchors.rogue.shared.utils.misc

import com.badlogic.gdx.math.Vector2

fun Any.interpolate(
    b: Any,
    t: Float,
): Any =
    when (this) {
        is Float if b is Float -> this + (b - this) * t
        is Vector2 if b is Vector2 ->
            Vector2(
                this.x + (b.x - this.x) * t,
                this.y + (b.y - this.y) * t,
            )
        else -> this // fallback: snap
    }

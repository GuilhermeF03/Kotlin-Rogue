package anchors.rogue.shared.utils.misc

import com.badlogic.gdx.math.Vector2

const val PPM = 64f   // 100 pixels = 1 meter


fun Float.toMeters() = this / PPM
fun Float.toPixels() = this * PPM

fun Vector2.toMeters(): Vector2 = Vector2(this.x / PPM, this.y / PPM)
fun Vector2.toPixels(): Vector2 = Vector2(this.x * PPM, this.y * PPM)



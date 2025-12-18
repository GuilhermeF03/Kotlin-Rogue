package anchors.framework.nodes.types.visual.animation.tracks

import anchors.framework.nodes.types.visual.AnimatedSprite2D
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion

class SpriteTrack(
    val sprite: AnimatedSprite2D,
    val frameDuration: Float,
) : Track<TextureRegion>() {
    val animation: Animation<TextureRegion>
        get() = Animation(frameDuration, *sprite.frames)

    override val maxLength: Float = animation.animationDuration

    override fun key(
        time: Float,
        value: TextureRegion,
    ) {
        TODO("Not yet implemented")
    }

    override fun collectUpdates(
        prevTime: Float,
        time: Float,
    ): List<() -> Unit> =
        listOf {
            sprite.currFrame = Sprite(animation.getKeyFrame(time))
        }
}

package anchors.framework.nodes.types.animation

import anchors.framework.nodes.types.animation.tracks.PropertyTrack
import anchors.framework.nodes.types.animation.tracks.SpriteTrack
import anchors.framework.nodes.types.animation.tracks.Track
import anchors.framework.nodes.types.visual.AnimatedSprite2D
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion


/**
 *
 */
class SpriteAnimation<T : TextureRegion>(
    name: String,
    val frames: Array<T>,
    playMode: PlayMode = PlayMode.NORMAL,
    block: SpriteAnimation<T>.() -> Unit = {},
) : Animation<SpriteAnimation<T>>(name, playMode, block)


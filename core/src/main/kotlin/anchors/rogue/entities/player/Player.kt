package anchors.rogue.entities.player

import anchors.rogue.shared.utils.nodes.types.physics.body.DynamicBody2D
import anchors.rogue.shared.utils.nodes.types.physics.fixture.Area2D
import anchors.rogue.shared.utils.nodes.types.physics.fixture.Collider2D
import anchors.rogue.shared.utils.nodes.types.physics.shape.BoxShape2D
import anchors.rogue.shared.utils.nodes.types.visual.Sprite2D
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import ktx.assets.toInternalFile


fun Player() = DynamicBody2D(
    "player",
    script = { node -> PlayerController(node) }
){
    val texture = Texture("logo.png".toInternalFile(), true).apply {
        setFilter(Linear, Linear)
    }
    Sprite2D("sprite", texture = texture)
    Area2D("body", shape = BoxShape2D(200f, 200f))
}

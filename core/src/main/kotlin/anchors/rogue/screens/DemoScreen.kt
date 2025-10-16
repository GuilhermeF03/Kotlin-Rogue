package anchors.rogue.screens

import anchors.rogue.ecs.coreWorld
import anchors.rogue.ecs.graphics.components.SpriteComponent
import anchors.rogue.ecs.graphics.entities.sprite2D
import anchors.rogue.ecs.systems.RenderSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.github.quillraven.fleks.World
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile

class DemoScreen : KtxScreen {
    private val world: World = coreWorld()

    // Screen resources
    val image = Texture("logo.png".toInternalFile(), true).apply {
        setFilter(Linear, Linear)
    }
    // Entities
    val renderableLogo = world.sprite2D(image,Vector2(100f, 100f)){
        // Additional configuration can be done here if needed
        it[SpriteComponent].apply {
            //image.translate(100f, 100f) // Move the logo by (100,100)
            image
        }
    }
    val a = world.sprite2D(image)

    // Similar to "onUpdate" in other engines
    override fun render(delta: Float) {
        super.render(delta)
       world.update(delta)
    }
    // Clean up resources when the screen is disposed
    override fun dispose() {
        super.dispose()
        image.disposeSafely()
        world.dispose()
    }
}

package anchors.rogue

import anchors.rogue.ecs.components.PositionComponent
import anchors.rogue.ecs.entities.sprite2D
import anchors.rogue.ecs.systems.RenderSystem
import anchors.rogue.ecs.systems.managers.ManagersRegistry
import anchors.rogue.features.logbook.LogBookManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.github.quillraven.fleks.configureWorld
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.async.KtxAsync

class KotlinRogue : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()

        // Register and setup global managers - these are not tied to the ECS world lifecycle
        ManagersRegistry.apply {
            register(LogBookManager())
        }.setup()

        addScreen(FirstScreen())
        setScreen<FirstScreen>()
    }
}

class FirstScreen : KtxScreen {
    private val image = Texture("logo.png".toInternalFile(), true).apply { setFilter(Linear, Linear) }

    // Stage is used for rendering 2D actors; here it's used for rendering sprites
    // We can use multiple stages for different layers or UI elements
    private val stage = Stage(ScreenViewport())

    // A world represents the ECS context, holding entities, components, and systems
    // Worlds should be screen-specific and disposed of when the screen is no longer needed
    private val world = configureWorld {
        // Register your components, systems, and injectables here so they can be used in the ECS
        injectables {
            add(stage)
        }
        // Register systems, which contain the game logic and operate on entities with specific components
        systems {
            add(RenderSystem())
        }
    }

    // Example of creating an entity using the sprite2D function
    val renderableLogo = world.sprite2D(
        image,
        position = Vector2(100f, 100f)
    ){
        // Additional configuration can be done here if needed
        it[PositionComponent].coords.add(50f,50f) // Move the logo by (50,50)
    }
    val a = world.sprite2D(
        image,
        position = Vector2(100f, 100f)
    )
    // Due to its 'family'(set of components), this entity will be processed by the RenderSystem
    // Example: A family of (A, B, C) components will be processed by a system that requires all three components
    // If an entity has only (A, B), it will not be processed by that system, unless the system is configured to accept any of those components



    // Similar to "onUpdate" in other engines
    override fun render(delta: Float) {
        super.render(delta)
        world.update(delta)
    }

    // Clean up resources when the screen is disposed
    override fun dispose() {
        image.disposeSafely()
        world.dispose()
        ManagersRegistry.teardown()
    }
}

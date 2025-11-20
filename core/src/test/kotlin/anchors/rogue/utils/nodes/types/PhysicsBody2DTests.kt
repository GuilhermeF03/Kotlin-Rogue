package anchors.rogue.utils.nodes.types

import anchors.rogue.shared.managers.ManagersRegistry
import anchors.rogue.shared.utils.nodes.SceneManager
import anchors.rogue.shared.utils.nodes.systems.PhysicsSystem
import anchors.rogue.shared.utils.nodes.types.physics.body.DynamicBody2D
import anchors.rogue.shared.utils.nodes.types.physics.fixture.Collider2D
import anchors.rogue.shared.utils.nodes.types.physics.body.StaticBody2D
import anchors.rogue.shared.utils.nodes.types.physics.fixture.Area2D
import anchors.rogue.shared.utils.nodes.types.physics.shape.BoxShape2D
import anchors.rogue.shared.utils.nodes.types.physics.shape.CircleShape2D
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3NativesLoader
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Shape
import kotlin.test.Test
import ktx.box2d.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.assertNotNull
import kotlin.test.assertEquals

class PhysicsBody2DTests {

    companion object {
        @BeforeAll
        @JvmStatic
        fun setup() {
            val sceneManager = SceneManager{
                addSystem(PhysicsSystem())
            }
            ManagersRegistry.register(sceneManager)

            sceneManager.setup()

            sceneManager.getSystem(PhysicsSystem::class).replaceWorld()
        }

        @BeforeAll
        @JvmStatic
        fun loadNatives() {
            Lwjgl3NativesLoader.load()
        }
    }

    @Test
    fun `fixture should add shape`(){
        val tree = DynamicBody2D("root"){
            Collider2D(
                name = "collider",
                shape = BoxShape2D(),
                position = Vector2(100f, 100f)
            )
            Area2D(
                name = "area",
                shape = CircleShape2D()
            )
        }
        tree.buildTree()

        assertEquals(2, tree.body.fixtureList.size)

        val body = tree.body
        val collider = tree.getNode<Collider2D>("collider")
        val area = tree.getNode<Area2D>("area")

        assertNotNull(collider)
        assertNotNull(area)

        body.fixtureList.forEach { fixture ->
            if(fixture.userData == collider){
                assertEquals(Shape.Type.Polygon, fixture.type)
            } else {
              assertEquals(Shape.Type.Circle, fixture.type)
            }
        }
    }

}

package anchors.rogue.features.saving

import anchors.rogue.ecs.managers.ManagersRegistry
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.builtins.serializer
import org.junit.jupiter.api.BeforeAll
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SaveManagerTests {

    companion object{
        val saveManager = SaveManager { slot ->
            val file = File("src/test/output/test-$slot.json")
            FileHandle(file)
        }

        @JvmStatic
        @BeforeAll
        fun setup(){
            ManagersRegistry.register(saveManager)
        }
    }

    @Test
    fun `should write empty file`(){
        // Act
        saveManager.save(0)
        // Assert
        val file = FileHandle(File("src/test/output/test-0.json"))
        assertTrue { file.exists() }
        assertEquals("{}", file.readString())
    }

    @Test
    fun `should write data`(){
        // Setup
        var intData = 0

        registerSaveModule(
            id = "test-int",
            serializer = Int.serializer(),
            onSave = {5},
            onLoad = {intData = it}
        )

        var stringData = ""
        registerSaveModule(
            id = "test-string",
            serializer = String.serializer(),
            onSave = {"abc"},
            onLoad = {stringData = it}
        )
        // Act
        saveManager.save(0)
        // Assert
        saveManager.load(0)
        assertEquals(5, intData)
        assertEquals("abc", stringData)
    }
}

package anchors.rogue.features.saving

import anchors.rogue.ecs.managers.ManagersRegistry
import anchors.rogue.utils.data.parsers.JsonParser
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import java.io.File
import java.nio.file.Files
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
        val intModule = object : SaveModule<Int>() {
            override val id: String = "test-int"
            override val serializer: KSerializer<Int> = Int.serializer()
            override fun save(): Int = 5
        }
        val stringModule = object : SaveModule<String>() {
            override val id: String = "test-string"
            override val serializer: KSerializer<String> = String.serializer()
            override fun save(): String = "abc"
        }
        // Act
        saveManager.save(0)
        // Assert
        saveManager.load(0)
        val intData = saveManager.loadData<Int>(intModule.id)
        val stringData = saveManager.loadData<String>(stringModule.id)
        assertEquals(5, intData)
        assertEquals("abc", stringData)
    }
}

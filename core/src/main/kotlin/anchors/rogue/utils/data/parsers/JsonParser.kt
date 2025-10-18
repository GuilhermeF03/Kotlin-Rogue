package anchors.rogue.utils.data.parsers

import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

/**
 * A utility object for parsing JSON data from files or strings into Kotlin objects.
 * Allows callers to provide their own [SerializersModule] for custom polymorphic serialization.
 */
/**
 * A generic JSON parsing utility with optional polymorphic support.
 */
object JsonParser {

    fun createJson(module: SerializersModule?) = Json {
        if (module != null) serializersModule = module
        classDiscriminator = "type"
        ignoreUnknownKeys = true
        prettyPrint = false
    }

    inline fun <reified T> parseFile(file: FileHandle, module: SerializersModule? = null): T {
        val json = createJson(module)
        return json.decodeFromString(file.file().readText())
    }

    inline fun <reified T> parseString(jsonString: String, module: SerializersModule? = null): T {
        val json = createJson(module)
        return json.decodeFromString(jsonString)
    }
}


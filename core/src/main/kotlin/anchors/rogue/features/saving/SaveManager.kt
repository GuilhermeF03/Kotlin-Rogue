package anchors.rogue.features.saving

import anchors.rogue.ecs.managers.Manager
import anchors.rogue.utils.data.parsers.JsonParser
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.serializerOrNull

const val SAVE_LOCATION = "saves"

class SaveManager(
    val fileFactory: (slot: Int) -> FileHandle = {
        Gdx.files.local("$SAVE_LOCATION/user-$it.json")
    }
) : Manager() {

    private val modules: MutableList<SaveModule<*>> = mutableListOf()
    val dataRegistry: MutableMap<String, @Serializable Any> = mutableMapOf()

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    // -------------------------------
    // REGISTER MODULE
    // -------------------------------
    fun <T : @Serializable Any> register(module: SaveModule<T>) {
        modules += module
    }

    // -------------------------------
    // SAVE
    // -------------------------------
    @OptIn(InternalSerializationApi::class)
    fun save(slot: Int) {
        // Collect all data from modules
        modules.forEach { module ->
            dataRegistry[module.id] = module.save()
        }

        // Encode all entries safely
        val root = buildJsonObject {
            dataRegistry.forEach { (key, value) ->
                val serializer = value::class.serializerOrNull()
                    ?: error("No serializer found for class ${value::class.simpleName}")
                put(key, json.encodeToJsonElement(serializer as KSerializer<Any> , value))
            }
        }

        // Save to disk
        JsonParser.toFile(root, fileFactory(slot))
    }

    // -------------------------------
    // LOAD
    // -------------------------------
    fun load(slot: Int) {
        val jsonData = JsonParser.rawParseFile(fileFactory(slot))
        modules.forEach { module ->
            jsonData[module.id]?.let { element ->
                module.loadFromJson(element)
            }
        }
    }
}

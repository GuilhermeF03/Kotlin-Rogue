package anchors.rogue.features.saving

import anchors.rogue.ecs.managers.Manager
import anchors.rogue.utils.data.parsers.JsonParser
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

const val SAVE_LOCATION = "saves"

class SaveManager(
    private val saveFileFactory: (slot: Int) -> FileHandle = { Gdx.files.local("$SAVE_LOCATION/user_save_$it.json") },
) : Manager() {
    private val dataRegistry = mutableMapOf<SaveModule<*>, @Serializable Any>()
    private val json: Json = Json { prettyPrint = true }

    fun register(module: SaveModule<*>) {
        dataRegistry[module] = Unit // placeholder
    }

    fun load(slot: Int) {
        val fileLocation = saveFileFactory(slot)
        val jsonData = JsonParser.rawParseFile(fileLocation)

        for ((module, _) in dataRegistry) {
            val jsonElement = jsonData[module.id] ?: continue

            // Deserialize using correct generic handling
            @Suppress("UNCHECKED_CAST")
            val typedModule = module as SaveModule<Any>
            val decodedData = json.decodeFromJsonElement(typedModule.serializer, jsonElement)

            dataRegistry[typedModule] = decodedData
            typedModule.onLoad(decodedData)
        }
    }

    fun save(slot: Int) {
        val saveLocation = saveFileFactory(slot)
        val jsonMap =
            buildMap {
                for (module in dataRegistry.keys) {
                    @Suppress("UNCHECKED_CAST")
                    val typedModule = module as SaveModule<Any>
                    val data = typedModule.onSave()
                    put(typedModule.id, json.encodeToJsonElement(typedModule.serializer, data))
                }
            }
        val jsonData = JsonObject(jsonMap)
        JsonParser.toFile(jsonData, saveLocation)
    }
}

package anchors.rogue.features.saving

import anchors.rogue.shared.ecs.managers.Manager
import anchors.rogue.shared.utils.data.parsers.JsonParser
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlin.reflect.KClass

const val SAVE_LOCATION = "saves"

/**
 * Responsible for handling data saving and loading.
 *
 * The structure of data serialization and deserialization are configured through individual modules.
 *
 * @see SaveModule
 */
class SaveManager(
    // Handler for configuring the save file location - used this as opposed to string for testing purposes
    private val saveFileFactory: (slot: Int) -> FileHandle =
        { Gdx.files.local("$SAVE_LOCATION/user_save_$it.json") },
) : Manager() {
    /** Holds module info, and maps to data to be saved */
    private val dataRegistry = mutableMapOf<SaveModule<*>, @Serializable Any>()

    // Register new save module
    fun register(module: SaveModule<*>) {
        dataRegistry[module] = Unit // placeholder
    }

    /**
     * Loads data from a given save slot.
     *
     * Each registered module has its onLoad method called, with the parsed data passed directly.
     */
    @Suppress("UNCHECKED_CAST")
    fun load(slot: Int) {
        val fileLocation = saveFileFactory(slot)
        val jsonData = JsonParser.rawParseFile(fileLocation)

        dataRegistry.keys.forEach { module ->
            val jsonElement = jsonData[module.id] ?: return@forEach // continue

            // Deserialize using correct generic handling
            val typedModule = module as SaveModule<Any>
            val decodedData = JsonParser.decodeJsonElement(typedModule.serializer, jsonElement)

            dataRegistry[typedModule] = decodedData // load data into registry
            typedModule.onLoad(decodedData)
        }
    }

    /**
     * Loads specific data - useful for reading data after initial load.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> loadData(clazz: KClass<T>): T = dataRegistry.values.first { it::class == clazz } as T

    /**
     * Saves data into a given slot.
     *
     * Json structure is defined based on registration order.
     *
     * Data to be saved on each module is defined by the return value of the **onSave** method.
     */
    fun save(slot: Int) {
        val saveLocation = saveFileFactory(slot)
        val jsonMap =
            buildMap {
                dataRegistry.keys.forEach { module ->
                    @Suppress("UNCHECKED_CAST")
                    val typedModule = module as SaveModule<Any>
                    val data = typedModule.onSave()
                    put(typedModule.id, JsonParser.encodeJsonElement(typedModule.serializer, data))
                }
            }
        val jsonData = JsonObject(jsonMap)
        JsonParser.toFile(jsonData, saveLocation)
    }
}

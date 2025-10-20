package anchors.rogue.utils.data.registry

import anchors.rogue.utils.data.parsers.JsonParser
import com.badlogic.gdx.files.FileHandle
import kotlin.reflect.KClass

/**
 * Loads defined items from .json files into registry maps.
 * Useful for mapping ids into concrete objects
 */
class JsonIdRegistry<T : IdEntry>(
    private val source : FileHandle? = null // null for tests
) {
    val map : MutableMap<KClass<out T>, MutableMap<String, T>> = mutableMapOf()

    init{
        if(source != null)
            check(source.exists()) {"'${source.path()}' not found!"}
    }

    /**
     * Loads registry - either by passing a list of items,
     * or passing **null** if to load from 'source' field of the repository
     */
    fun loadRegistry(registryItems: List<T>? = null){
        if(registryItems != null){
            addItemsToRegistry(registryItems)
            return
        }

        check(source != null) {"No registry items passed - source shouldn't be null!"}

        val jsonFiles = if( source.isDirectory) collectJsonFiles(source) else listOf(source)

        jsonFiles.forEach { file ->
            val items: List<T> = JsonParser.parseFile(file)
            addItemsToRegistry(items)
        }
    }

    private  fun addItemsToRegistry(items: List<T>) {
        items.forEach { item -> map.getOrPut(item::class) { mutableMapOf() }[item.id] = item }
    }

    private fun collectJsonFiles(dir: FileHandle): List<FileHandle> {
        return dir.list()?.flatMap { file ->
            when {
                file.isDirectory -> collectJsonFiles(file) // recurse into subfolders
                file.extension() == "json" -> listOf(file)
                else -> emptyList()
            }
        } ?: emptyList()
    }

    inline fun <reified R : T> mapItems(ids: List<String>): List<R> {
        if (ids.isEmpty()) return emptyList()

        val registry = map[R::class] ?: throw IllegalArgumentException(
            "No items of type '${R::class.simpleName}' found in registry"
        )

        return ids.map { id ->
            val item = registry[id] ?: throw IllegalArgumentException(
                "Id '$id' not found in registry '${R::class.simpleName}'"
            )
            item as R
        }
    }
}

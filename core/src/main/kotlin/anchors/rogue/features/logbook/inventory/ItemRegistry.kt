package anchors.rogue.features.logbook.inventory

import anchors.rogue.utils.data.parsers.JsonParser
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle

/**
 * Loads defined items from .json files into registry maps.
 * Maps ids from the inventory save data into items.
 */
open class ItemRegistry {
    val trinketsMap : MutableMap<String, Item.Trinket> = mutableMapOf()
    val consumableMap : MutableMap<String, Item.Consumable> = mutableMapOf()

    val weaponsMap : MutableMap<String, EquippableItem.Weapon> = mutableMapOf()
    val armorsMap : MutableMap<String, EquippableItem.Armor> = mutableMapOf()
    val accessoryMap : MutableMap<String, EquippableItem.Accessory> = mutableMapOf()

    init {
        loadRegistry()
    }

    /**
     * Loads all items defined in .json files in /data/items recursively
     * @param testItems - items passed during unit tests
     */
    fun loadRegistry(testItems : List<Item>? = null){
        if(testItems != null){
            addItemsToRegistry(testItems)
            return
        }

        // Path to the folder on disk (adjust as needed)
        val folder = Gdx.files.internal("data/items")
        require(folder.exists() && folder.isDirectory) { "Expected folder at ${folder.path()}" }

        // Recursively collect all JSON files
        fun collectJsonFiles(dir: FileHandle): List<FileHandle> {
            return dir.list()?.flatMap { file ->
                when {
                    file.isDirectory -> collectJsonFiles(file) // recurse into subfolders
                    file.extension() == "json" -> listOf(file)
                    else -> emptyList()
                }
            } ?: emptyList()
        }

        val jsonFiles = collectJsonFiles(folder)

        // Parse and register items
        jsonFiles.forEach { file ->
            val items: List<Item> = JsonParser.parseFile(file)
            addItemsToRegistry(items)
        }
    }

    private fun addItemsToRegistry(items: List<Item>) {
        items.forEach { item ->
            when (item) {
                is EquippableItem.Weapon -> weaponsMap[item.id] = item
                is EquippableItem.Armor -> armorsMap[item.id] = item
                is EquippableItem.Accessory -> accessoryMap[item.id] = item
                is Item.Trinket -> trinketsMap[item.id] = item
                is Item.Consumable -> consumableMap[item.id] = item
            }
        }
    }

    inline fun <reified T : Item> mapItems(ids : List<String>): List<T> {
        if(ids.isEmpty()) return listOf()

        val registry: Map<String, Item> = when (T::class) {
            EquippableItem.Weapon::class -> weaponsMap
            EquippableItem.Armor::class -> armorsMap
            EquippableItem.Accessory::class -> accessoryMap
            Item.Trinket::class -> trinketsMap
            Item.Consumable::class -> consumableMap
            else -> throw IllegalArgumentException("Invalid item type: ${T::class.simpleName}")
        }

        return ids.map { id ->
            val item = registry[id] ?: throw IllegalArgumentException(
                "Id '$id' not found in registry '${T::class.simpleName}'"
            )
            require(item is T) { "Item '$item' not of expected type '${T::class.simpleName}'" }
            item
        }
    }
}

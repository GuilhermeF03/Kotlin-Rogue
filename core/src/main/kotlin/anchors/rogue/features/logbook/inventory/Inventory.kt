package anchors.rogue.features.logbook.inventory

data class Inventory (
    val equippedItems: List<Item> = listOf(),
    val storedItems: List<Item> = listOf(),
)

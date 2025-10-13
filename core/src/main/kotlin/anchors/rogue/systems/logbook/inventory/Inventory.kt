package anchors.rogue.systems.logbook.inventory

data class Inventory (
    val equippedItems: List<Item> = listOf(),
    val storedItems: List<Item> = listOf(),
)

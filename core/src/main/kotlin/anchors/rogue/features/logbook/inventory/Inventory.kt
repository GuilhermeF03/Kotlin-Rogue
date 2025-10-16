package anchors.rogue.features.logbook.inventory

import anchors.rogue.utils.signals.OneArgSignal
import anchors.rogue.utils.signals.createSignal

data class Inventory (
    val equippedItems: List<Item> = listOf(),
    val storedItems: List<Item> = listOf(),
    val onItemPickup : OneArgSignal<Item> = createSignal<Item>()
){
    fun pickupItem(item: Item) {
        onItemPickup.emit(item)
    }
}

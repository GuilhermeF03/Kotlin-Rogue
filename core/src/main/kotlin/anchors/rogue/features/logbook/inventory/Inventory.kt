package anchors.rogue.features.logbook.inventory

import anchors.rogue.utils.signals.Signal
import anchors.rogue.utils.signals.signal

data class Inventory (
    val equippedItems: List<Item> = listOf(),
    val storedItems: List<Item> = listOf(),
    val onItemPickup : Signal.OneArgSignal<Item> = signal<Item>()
){
    fun pickupItem(item: Item) {
        onItemPickup.emit(item)
    }
}

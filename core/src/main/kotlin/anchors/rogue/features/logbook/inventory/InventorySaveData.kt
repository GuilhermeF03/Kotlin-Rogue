package anchors.rogue.features.logbook.inventory

import anchors.rogue.items.EquippableItem
import kotlinx.serialization.Serializable

@Serializable
data class InventorySaveData(
    // Current gold amount
    val gold: Int = 0,
    // Currently equipped items
    val equipment : EquipmentData = EquipmentData(),
    // Stored items categorized by their type
    val trinkets: List<InventoryEntry> = listOf(),
    val consumables: List<InventoryEntry> = listOf(),
    val weapons: List<InventoryEntry> = listOf(),
    val armors: List<InventoryEntry> = listOf(),
    val accessories: List<InventoryEntry> = listOf(),
)

@Serializable
data class InventoryEntry(val id : String, val quantity : Int)

fun Inventory.asData() : InventorySaveData = InventorySaveData(
    gold.value,
    equipment,
    trinkets.map { InventoryEntry(it.id, it.quantity) },
    consumables.map { InventoryEntry(it.id, it.quantity) },
    weapons.map { InventoryEntry(it.id, it.quantity) },
    armors.map { InventoryEntry(it.id, it.quantity) },
    accessories.map { InventoryEntry(it.id, it.quantity) }
)

@Serializable
data class EquipmentData(
    // Currently equipped weapon
    val currWeapon: EquippableItem.Weapon? = null,
    // Currently equipped armor
    val currArmor: EquippableItem.Armor? = null,
    // Currently equipped accessory
    val currAccessory: EquippableItem.Accessory? = null,
)

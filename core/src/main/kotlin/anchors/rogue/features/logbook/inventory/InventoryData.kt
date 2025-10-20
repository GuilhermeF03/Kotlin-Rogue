package anchors.rogue.features.logbook.inventory

import anchors.rogue.items.EquippableItem
import kotlinx.serialization.Serializable

@Serializable
data class InventoryData(
    // Current gold amount
    val gold: Int = 0,
    // Currently equipped items
    val equipment : EquipmentData = EquipmentData(),
    // Stored items categorized by their type
    val trinkets: List<String> = listOf(),
    val consumables: List<String> = listOf(),
    val weapons: List<String> = listOf(),
    val armors: List<String> = listOf(),
    val accessories: List<String> = listOf(),
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

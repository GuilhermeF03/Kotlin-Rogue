package anchors.rogue.features.logbook.inventory

import kotlinx.serialization.Serializable

@Serializable
data class InventoryData(
    val gold: Int = 0,

    // Currently equipped items
    val equipment : EquipmentData = EquipmentData(),

    // Stored items categorized by their type
    val trinkets: MutableList<Item.Trinket> = mutableListOf(),
    val consumables: MutableList<Item.Consumable> = mutableListOf(),

    val weapons: MutableList<EquippableItem.Weapon> = mutableListOf(),
    val armors: MutableList<EquippableItem.Armor> = mutableListOf(),
    val accessories: MutableList<EquippableItem.Accessory> = mutableListOf(),
)

@Serializable
data class EquipmentData(
    var currWeapon: EquippableItem.Weapon? = null,
    var currArmor: EquippableItem.Armor? = null,
    var currAccessory: EquippableItem.Accessory? = null,
)

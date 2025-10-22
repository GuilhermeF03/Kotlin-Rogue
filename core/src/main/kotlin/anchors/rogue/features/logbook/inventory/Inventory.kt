package anchors.rogue.features.logbook.inventory

import anchors.rogue.features.saving.registerSaveModule
import anchors.rogue.items.EquippableItem
import anchors.rogue.items.Item
import anchors.rogue.utils.data.registry.IdRegistry
import anchors.rogue.utils.signals.OneArgSignal
import anchors.rogue.utils.signals.SignalVal
import anchors.rogue.utils.signals.asSignalVal
import anchors.rogue.utils.signals.createSignal
import com.badlogic.gdx.Gdx


const val ITEMS_JSON = "data/items"

class Inventory(
    private val registry: IdRegistry<Item> =
        IdRegistry<Item>(Gdx.files.internal(ITEMS_JSON)).also { it.loadRegistry()}
){
    // Amount of gold the player has
    val gold: SignalVal<Int> = 0.asSignalVal()
    // Currently equipped items
    var equipment : EquipmentData = EquipmentData()
    // Stored items categorized by their type
    val trinkets: MutableList<Item.Trinket> = mutableListOf()
    val consumables: MutableList<Item.Consumable> = mutableListOf()
    val weapons: MutableList<EquippableItem.Weapon> = mutableListOf()
    val armors: MutableList<EquippableItem.Armor> = mutableListOf()
    val accessories: MutableList<EquippableItem.Accessory> = mutableListOf()
    // Signals - events that can be listened to
    val onItemPick : OneArgSignal<Item> = createSignal<Item>()
    val onItemDrop : OneArgSignal<Item> = createSignal<Item>()
    val onItemSold : OneArgSignal<Item> = createSignal<Item>()
    val onEquip : OneArgSignal<EquippableItem> = createSignal<EquippableItem>()
    val onUnequip : OneArgSignal<EquippableItem> = createSignal<EquippableItem>()
    val onUseItem : OneArgSignal<Item.Consumable> = createSignal<Item.Consumable>()

    init {
        registerSaveModule<InventorySaveData>(
            id = "inventory",
            serializer = InventorySaveData.serializer(),
            onSave = {this.asData()},
            onLoad = ::loadData
        )
    }
    /**
     * Loads inventory data from the provided InventoryData object.
     * This method populates the inventory with gold, equipped items, and stored items.
     * Used when loading a saved game.
     * @param data The InventoryData object containing the inventory information to load.
     */
    private fun loadData(data: InventorySaveData) {
        // Creates a new registry for mapping items' ids to item data
        gold.value = data.gold
        equipment = data.equipment

        val trinketQuantities = data.trinkets.associateBy({ it.id }, { it.quantity })
        trinkets.clear()
        trinkets += registry.mapIds(data.trinkets.map { it.id }) {
            copy(quantity = trinketQuantities[id] ?: quantity)
        }
        val consumableQuantities = data.consumables.associateBy({ it.id }, { it.quantity })
        consumables.clear()
        consumables += registry.mapIds(data.consumables.map { it.id }) {
            copy(quantity = consumableQuantities[id] ?: quantity)
        }
        weapons.clear()
        weapons += registry.mapIds(data.weapons.map { it.id })
        armors.clear()
        armors += registry.mapIds(data.armors.map { it.id })
        accessories.clear()
        accessories += registry.mapIds(data.accessories.map { it.id })
    }
    /**
     * Adds the specified amount of gold to the inventory.
     */
    fun addGold(amount: Int) {
        require(amount > 0){"Amount to add must be positive."}
        gold.value += amount
    }
    /**
     * Removes the specified amount of gold from the inventory.
     */
    fun removeGold(amount: Int) {
        require(amount > 0){"Amount to remove must be positive."}
        gold.value = 0.coerceAtLeast(gold.value - amount)
    }
    /** ========================================================================
     *                        Item Management Methods
     *  ===================================================================== */
    /**
     * Call this method when the player picks up an item.
     * It emits the onPickItem signal with the picked item.
     */
    fun pickItem(item: Item) {
        when(item) {
            is Item.Trinket -> trinkets.add(item)
            is Item.Consumable -> consumables.add(item)
            is EquippableItem.Weapon -> weapons.add(item)
            is EquippableItem.Armor -> armors.add(item)
            is EquippableItem.Accessory -> accessories.add(item)
        }
        onItemPick.emit(item)
    }

    /**
     * Called when item is dropped
     */
    fun dropItem(item: Item) {
        when(item) {
            is Item.Trinket -> trinkets.remove(item)
            is Item.Consumable -> consumables.remove(item)
            is EquippableItem.Weapon -> weapons.remove(item)
            is EquippableItem.Armor -> armors.remove(item)
            is EquippableItem.Accessory -> accessories.remove(item)
        }
        onItemDrop.emit(item)
    }
    /**
     * Sells the specified item from the inventory.
     * Removes the item from storedItems and adds its sell value to gold.
     * Emits the onSellItem signal with the sold item.
     */
    fun sellItem(item: Item) {
        when(item) {
            is Item.Trinket -> check(trinkets.remove(item)) {"Item not found in inventory."}
            is Item.Consumable -> check(consumables.remove(item)) {"Item not found in inventory."}
            is EquippableItem.Weapon -> check(weapons.remove(item)) {"Item not found in inventory."}
            is EquippableItem.Armor -> check(armors.remove(item)) {"Item not found in inventory."}
            is EquippableItem.Accessory -> check(accessories.remove(item)) {"Item not found in inventory."}
        }
        val sellValue = item.sellValue
        gold.value += sellValue
        onItemSold.emit(item)
    }
    /** ========================================================================
     *                        Equipment Management Methods
     *  ===================================================================== */
    /**
     * Equips the specified equippable item.
     * Updates the equipment slots and emits the onEquip signal.
     * @param item The equippable item to be equipped.
     */
    fun equipItem(item: EquippableItem) {
        when(item) {
            is EquippableItem.Weapon -> {
                check(item in weapons){"Item not found in inventory."}
                equipment = equipment.copy(currWeapon = item)
            }
            is EquippableItem.Armor -> {
                check(item in armors){"Item not found in inventory."}
                equipment = equipment.copy(currArmor = item)
            }
            is EquippableItem.Accessory -> {
                check(item in accessories){"Item not found in inventory."}
                equipment = equipment.copy(currAccessory = item)
            }
        }
        onEquip.emit(item)
    }
    /**
     * Unequips the specified equippable item.
     * Updates the equipment slots and emits the onUnequip signal.
     * This is a generic method that infers the type of item to unequip based on the reified type parameter T.
     * This blocks any attempt to unequip an item type that is not currently equipped.
     * @param T The type of equippable item to be unequipped (Weapon, Armor, or Accessory).
     * @throws IllegalStateException if no item of type T is currently equipped or if T is an unknown item type.
     */
    inline fun <reified T: EquippableItem> unequipItem() {
        val item : EquippableItem = when (T::class) {
            EquippableItem.Weapon::class -> {
                val curr = equipment.currWeapon ?: throw IllegalStateException("No weapon is currently equipped.")
                equipment = equipment.copy(currWeapon = null)
                curr
            }
            EquippableItem.Armor::class -> {
                val curr = equipment.currArmor ?: throw IllegalStateException("No armor is currently equipped.")
                equipment = equipment.copy(currArmor = null)
                curr
            }
            EquippableItem.Accessory::class -> {
                val curr = equipment.currAccessory ?: throw IllegalStateException("No accessory is currently equipped.")
                equipment = equipment.copy(currAccessory = null)
                curr
            }
            else -> throw IllegalStateException("Unknown item type: ${T::class}")
        }
        onUnequip.emit(item)
    }
    /**
     * Uses the specified consumable item.
     * Removes the item from the inventory and emits the onUseItem signal.
     * @param item The consumable item to be used.
     */
    fun useConsumable(item: Item.Consumable) {
        check(item in consumables){"Item not found in inventory."}
        consumables.remove(item)
        onUseItem.emit(item)
    }
}

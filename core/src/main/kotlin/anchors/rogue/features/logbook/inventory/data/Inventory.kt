package anchors.rogue.features.logbook.inventory.data

import anchors.rogue.features.saving.registerSaveModule
import anchors.rogue.items.EquippableItem
import anchors.rogue.items.Item
import anchors.rogue.shared.utils.data.registry.IdRegistry
import anchors.rogue.shared.utils.signals.OneArgSignal
import anchors.rogue.shared.utils.signals.SignalVal
import anchors.rogue.shared.utils.signals.asSignalVal
import anchors.rogue.shared.utils.signals.createSignal
import com.badlogic.gdx.Gdx
import kotlin.reflect.KClass

const val ITEMS_JSON = "data/items"

/**
 * Represents the player inventory - items, trinkets, equipment...
 */
class Inventory(
    private val registry: IdRegistry<Item> =
        IdRegistry<Item>(Gdx.files.internal(ITEMS_JSON)).also { it.loadRegistry<Item>() },
) {
    // Amount of gold the player has
    val gold: SignalVal<Int> = 0.asSignalVal()

    // Currently equipped items
    val currWeapon: SignalVal<EquippableItem.Weapon?> = null.asSignalVal()
    val currArmor: SignalVal<EquippableItem.Armor?> = null.asSignalVal()
    val currAccessory: SignalVal<EquippableItem.Accessory?> = null.asSignalVal()

    // Stored items categorized by their type
    val trinkets: MutableList<Item.Trinket> = mutableListOf()
    val consumables: MutableList<Item.Consumable> = mutableListOf()
    val weapons: MutableList<EquippableItem.Weapon> = mutableListOf()
    val armors: MutableList<EquippableItem.Armor> = mutableListOf()
    val accessories: MutableList<EquippableItem.Accessory> = mutableListOf()

    // Signals - events that can be listened to
    val onItemPick: OneArgSignal<Item> = createSignal<Item>()
    val onItemDrop: OneArgSignal<Item> = createSignal<Item>()
    val onItemSold: OneArgSignal<Item> = createSignal<Item>()

    // Equipment
    val onUseItem: OneArgSignal<Item.Consumable> = createSignal<Item.Consumable>()

    init {
        registerSaveModule<InventorySaveData>(
            id = "inventory",
            serializer = InventorySaveData.serializer(),
            onSave = { this.asData() },
            onLoad = ::loadData,
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

        currWeapon.value = data.equipment.currWeapon
        currArmor.value = data.equipment.currArmor
        currAccessory.value = data.equipment.currAccessory

        val trinketQuantities = data.trinkets.associate { it.id to it.quantity }
        fillListWithQuantities(trinkets, data.trinkets) {
            copy(quantity = trinketQuantities[id] ?: quantity)
        }

        val consumableQuantities = data.consumables.associate { it.id to it.quantity }
        fillListWithQuantities(trinkets, data.consumables) {
            copy(quantity = consumableQuantities[id] ?: quantity)
        }

        // Equipment
        fillListWithQuantities(weapons, data.weapons)
        fillListWithQuantities(armors, data.armors)
        fillListWithQuantities(accessories, data.accessories)
    }

    /**
     * Adds the specified amount of gold to the inventory.
     */
    fun addGold(amount: Int) {
        require(amount > 0) { "Amount to add must be positive." }
        gold.value += amount
    }

    /**
     * Removes the specified amount of gold from the inventory.
     */
    fun removeGold(amount: Int) {
        require(amount > 0) { "Amount to remove must be positive." }
        gold.value = 0.coerceAtLeast(gold.value - amount)
    }

    /**
     * Call this method when the player picks up an item.
     * It emits the onPickItem signal with the picked item.
     */
    fun pickItem(item: Item) {
        when (item) {
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
        when (item) {
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
        when (item) {
            is Item.Trinket ->
                check(trinkets.remove(item)) { "Item not found in inventory." }
            is Item.Consumable ->
                check(consumables.remove(item)) { "Item not found in inventory." }
            is EquippableItem.Weapon ->
                check(weapons.remove(item)) { "Item not found in inventory." }
            is EquippableItem.Armor ->
                check(armors.remove(item)) { "Item not found in inventory." }
            is EquippableItem.Accessory ->
                check(accessories.remove(item)) { "Item not found in inventory." }
        }
        val sellValue = item.sellValue
        gold.value += sellValue
        onItemSold.emit(item)
    }

    /**
     * Equips the specified equippable item.
     * Updates the equipment slots and emits the onEquip signal.
     * @param item The equippable item to be equipped.
     */
    fun equipItem(item: EquippableItem) {
        when (item) {
            is EquippableItem.Weapon -> {
                check(item in weapons) { "Item not found in inventory." }
                currWeapon.value = item
            }
            is EquippableItem.Armor -> {
                check(item in armors) { "Item not found in inventory." }
                currArmor.value = item
            }
            is EquippableItem.Accessory -> {
                check(item in accessories) { "Item not found in inventory." }
                currAccessory.value = item
            }
        }
    }

    /**
     * Unequips the specified equippable item.
     * Updates the equipment slots and emits the onUnequip signal.
     * This is a generic method that infers the type of item to unequip based on the reified type parameter T.
     * This blocks any attempt to unequip an item type that is not currently equipped.
     * @param T The type of equippable item to be unequipped (Weapon, Armor, or Accessory).
     * @throws IllegalStateException if no item of type T is currently equipped or if T is an unknown item type.
     */
    fun <T : EquippableItem> unequipItem(kClass: KClass<T>) {
        when (kClass) {
            EquippableItem.Weapon::class -> {
                checkNotNull(currWeapon.value) { "No weapon is currently equipped" }
                currWeapon.value = null
            }
            EquippableItem.Armor::class -> {
                checkNotNull(currArmor.value) { "No armor is currently equipped" }
                currArmor.value = null
            }
            EquippableItem.Accessory::class -> {
                checkNotNull(currAccessory.value) { "No accessory is currently equipped" }
                currAccessory.value = null
            }
            else -> throw IllegalStateException("Unknown item type: $kClass")
        }
    }

    /**
     * Uses the specified consumable item.
     * Removes the item from the inventory and emits the onUseItem signal.
     * @param item The consumable item to be used.
     */
    fun useConsumable(item: Item.Consumable) {
        check(item in consumables) { "Item not found in inventory." }
        consumables.remove(item)
        onUseItem.emit(item)
    }

    // ============ HELPER FUNCTIONS ===================
    private inline fun <reified T : Item> fillListWithQuantities(
        list: MutableList<T>,
        dataList: List<InventoryEntry>,
        copyMethod: T.() -> Unit = {},
    ) {
        list.clear()
        list += registry.mapIds(dataList.map { it.id }, copyMethod)
    }
}

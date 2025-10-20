package anchors.rogue.features.logbook.inventory

import anchors.rogue.utils.data.registry.JsonIdRegistry
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContentEquals

class ItemRegistryTests {
    val sword = EquippableItem.Weapon(name = "sword", maxDamage = 3)
    val armor = EquippableItem.Armor(name = "armor", sellValue = 10)
    val consumable = Item.Consumable(name = "consumable", description = "abc")

    val registry : JsonIdRegistry<Item> = JsonIdRegistry()

    @Test
    fun `should be unable to find any item`(){
        // Assert
        assertAll(
            { assertThrows<IllegalArgumentException> {
                registry.mapItems<EquippableItem.Weapon>(listOf(sword.id))
            }},
            { assertThrows<IllegalArgumentException> {
                registry.mapItems<EquippableItem.Armor>(listOf(armor.id))
            }},
            { assertThrows<IllegalArgumentException> {
                registry.mapItems<Item.Consumable>(listOf(consumable.id))
            }}
        )
    }
    @Test
    fun `should correctly map items`(){
        // Setup
        val items = listOf(sword.copy(), armor.copy(), consumable.copy())
        registry.loadRegistry(items)

        // Act
        val weapons : List<EquippableItem.Weapon> = registry.mapItems(listOf(sword.id))
        val armors : List<EquippableItem.Armor> = registry.mapItems(listOf(armor.id))
        val consumables : List<Item.Consumable> = registry.mapItems(listOf(consumable.id))

        assertContentEquals(listOf(sword), weapons)
        assertContentEquals(listOf(armor), armors)
        assertContentEquals(listOf(consumable), consumables)
    }
}

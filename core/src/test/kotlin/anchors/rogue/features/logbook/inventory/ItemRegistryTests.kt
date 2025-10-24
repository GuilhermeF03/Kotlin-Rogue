package anchors.rogue.features.logbook.inventory

import anchors.rogue.items.EquippableItem
import anchors.rogue.items.Item
import anchors.rogue.shared.utils.data.registry.IdRegistry
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertContentEquals

class ItemRegistryTests {
    val sword = EquippableItem.Weapon(name = "sword", maxDamage = 3)
    val armor = EquippableItem.Armor(name = "armor", sellValue = 10)
    val consumable = Item.Consumable(name = "consumable", description = "abc", quantity = 3)

    val registry: IdRegistry<Item> = IdRegistry()

    @Test
    fun `should be unable to find any item`() {
        // Assert
        assertAll(
            {
                assertThrows<IllegalArgumentException> {
                    registry.mapIds<EquippableItem.Weapon>(listOf(sword.id))
                }
            },
            {
                assertThrows<IllegalArgumentException> {
                    registry.mapIds<EquippableItem.Armor>(listOf(armor.id))
                }
            },
            {
                assertThrows<IllegalArgumentException> {
                    registry.mapIds<Item.Consumable>(listOf(consumable.id))
                }
            },
        )
    }

    @Test
    fun `should correctly map items`() {
        // Setup
        val items = listOf(sword, armor, consumable)
        registry.loadRegistry(items)

        // Act
        val weapons: List<EquippableItem.Weapon> = registry.mapIds(listOf(sword.id))
        val armors: List<EquippableItem.Armor> = registry.mapIds(listOf(armor.id))
        val consumables: List<Item.Consumable> = registry.mapIds(listOf(consumable.id))

        assertContentEquals(listOf(sword), weapons)
        assertContentEquals(listOf(armor), armors)
        assertContentEquals(listOf(consumable), consumables)
    }
}

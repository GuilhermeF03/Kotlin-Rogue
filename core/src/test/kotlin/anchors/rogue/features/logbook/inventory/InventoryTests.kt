@file:Suppress("ktlint:standard:no-wildcard-imports")

package anchors.rogue.features.logbook.inventory

import anchors.rogue.features.logbook.inventory.data.Inventory
import anchors.rogue.features.saving.SaveManager
import anchors.rogue.features.stats.data.Stats
import anchors.rogue.items.EquippableItem
import anchors.rogue.shared.ecs.managers.ManagersRegistry
import anchors.rogue.shared.utils.data.registry.IdRegistry
import org.junit.jupiter.api.*
import kotlin.test.Test
import kotlin.test.assertEquals

@DisplayName("Inventory Tests")
class InventoryTests {
    val inventory by lazy { Inventory(IdRegistry()) }

    companion object {
        val saveManager = SaveManager()

        @JvmStatic
        @BeforeAll
        fun setup() {
            ManagersRegistry.register(saveManager)
        }
    }

    @Nested
    @DisplayName("Inventory Item Management Tests")
    inner class ItemManagementTests {
        @Test
        fun `picking an item should add it to inventory and emit signal`() {
            val sword =
                EquippableItem.Weapon(
                    name = "Sword",
                    stats = Stats(),
                )
            var signalEmitted = false

            inventory.onItemPick.connect { item ->
                signalEmitted = true
                assert(item == sword)
            }

            inventory.weapons.add(sword)
            inventory.onItemPick.emit(sword)

            assert(inventory.weapons.contains(sword))
            assert(signalEmitted)
        }

        @Test
        fun `selling an item should remove it from inventory, add gold, and emit signal`() {
            val shield =
                EquippableItem.Armor(
                    name = "Shield",
                    stats = Stats(),
                    sellValue = 50,
                )
            inventory.armors.add(shield)
            var goldSignalEmitted = false
            var sellSignalEmitted = false
            inventory.gold.connect { newGold ->
                goldSignalEmitted = true
                assert(newGold == 50)
            }
            inventory.onItemSold.connect { item ->
                sellSignalEmitted = true
                assert(item == shield)
            }
            inventory.sellItem(shield)
            assert(!inventory.armors.contains(shield))
            assert(goldSignalEmitted)
            assert(sellSignalEmitted)
        }

        @Test
        fun `adding gold should increase gold and emit signal`() {
            var signalEmitted = false
            inventory.gold.connect { newGold ->
                signalEmitted = true
                assert(newGold == 200)
            }

            inventory.addGold(200)
            assert(inventory.gold.value == 200)
            assert(signalEmitted)
        }

        @Test
        fun `removing gold should decrease gold and emit signal`() {
            inventory.addGold(300)
            var signalEmitted = false
            inventory.gold.connect { newGold ->
                signalEmitted = true
                assert(newGold == 100)
            }

            inventory.removeGold(200)
            assert(inventory.gold.value == 100)
            assert(signalEmitted)
        }

        @Test
        fun `passing a negative number to removeGold should fail`() {
            assertThrows<IllegalArgumentException> { inventory.removeGold(-200) }
        }

        @Test
        fun `passing a negative number to addGold should fail`() {
            assertThrows<IllegalArgumentException> { inventory.addGold(-50) }
        }

        @Test
        fun `removing more gold than available should set gold to zero`() {
            inventory.addGold(100)
            inventory.removeGold(150)
            assert(inventory.gold.value == 0)
        }

        @Test
        fun `selling an item not in inventory should throw a illegal state exception`() {
            val ring =
                EquippableItem.Accessory(
                    name = "Ring",
                    stats = Stats(),
                    sellValue = 30,
                )
            assertThrows<IllegalStateException> { inventory.sellItem(ring) }
        }
    }

    @Nested
    @DisplayName("Inventory Equipment Management Tests")
    inner class EquipmentManagementTests {
        @Test
        fun `equipping a weapon should update current weapon and emit signal`() {
            val sword =
                EquippableItem.Weapon(
                    name = "Sword",
                    stats = Stats(),
                )
            var signalEmitted = false
            inventory.currWeapon connect { signalEmitted = true }

            inventory.weapons.add(sword)
            inventory.equipItem(sword)

            assertEquals(sword, inventory.currWeapon.value)
            assert(signalEmitted)
        }

        @Test
        fun `equipping an armor should update current armor and emit signal`() {
            val shield =
                EquippableItem.Armor(
                    name = "Shield",
                    stats = Stats(),
                )
            var signalEmitted = false
            inventory.currArmor.connect { signalEmitted = true }

            inventory.armors.add(shield)
            inventory.equipItem(shield)

            assertEquals(shield, inventory.currArmor.value)
            assert(signalEmitted)
        }

        @Test
        fun `unequipping an accessory should clear current accessory and emit signal`() {
            val ring =
                EquippableItem.Accessory(
                    name = "Ring",
                    stats = Stats(),
                )
            inventory.accessories.add(ring)
            inventory.equipItem(ring)

            var signalEmitted = false
            inventory.currAccessory.connect { signalEmitted = true }

            inventory.unequipItem(EquippableItem.Accessory::class)

            assertNull(inventory.currAccessory.value)
            assert(signalEmitted)
        }

        @Test
        fun `equipping an item not in inventory should throw an illegal state exception`() {
            val axe =
                EquippableItem.Weapon(
                    name = "Axe",
                    stats = Stats(),
                )
            assertThrows<IllegalStateException> { inventory.equipItem(axe) }
        }

        @Test
        fun `unequipping an item when none is equipped should throw an illegal state exception`() {
            assertThrows<IllegalStateException> { inventory.unequipItem(EquippableItem.Weapon::class) }
        }
    }
}

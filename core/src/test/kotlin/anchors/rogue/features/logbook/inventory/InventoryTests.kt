package anchors.rogue.features.logbook.inventory

import anchors.rogue.features.stats.data.Stats
import anchors.rogue.items.EquippableItem
import anchors.rogue.utils.data.registry.IdRegistry
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

@DisplayName("Inventory Tests")
class InventoryTests {
    val inventory = InventoryManager { IdRegistry() }

    @Nested
    @DisplayName("Inventory Item Management Tests")
    inner class ItemManagementTests {
        @Test
        fun `picking an item should add it to inventory and emit signal`() {
            val sword = EquippableItem.Weapon(
                name = "Sword",
                stats = Stats(),
            )
            var signalEmitted = false

            inventory.onPickItem.connect { item ->
                signalEmitted = true
                assert(item == sword)
            }

            inventory.weapons.add(sword)
            inventory.onPickItem.emit(sword)

            assert(inventory.weapons.contains(sword))
            assert(signalEmitted)
        }

        @Test
        fun `selling an item should remove it from inventory, add gold, and emit signal`() {
            val shield = EquippableItem.Armor(
                name = "Shield",
                stats = Stats(),
                sellValue = 50
            )
            inventory.armors.add(shield)
            var goldSignalEmitted = false
            var sellSignalEmitted = false
            inventory.gold.connect { newGold ->
                goldSignalEmitted = true
                assert(newGold == 50)
            }
            inventory.onSellItem.connect { item ->
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
            val ring = EquippableItem.Accessory(
                name = "Ring",
                stats = Stats(),
                sellValue = 30
            )
            assertThrows<IllegalStateException> { inventory.sellItem(ring) }
        }
    }

    @Nested
    @DisplayName("Inventory Equipment Management Tests")
    inner class EquipmentManagementTests {
        @Test
        fun `equipping a weapon should update current weapon and emit signal`() {
            val sword = EquippableItem.Weapon(
                name = "Sword",
                stats = Stats(),
            )
            var signalEmitted = false
            inventory.onEquip.connect { item ->
                signalEmitted = true
                assert(item == sword)
            }

            inventory.weapons.add(sword)
            inventory.equipItem(sword)

            assert(inventory.equipment.currWeapon == sword)
            assert(signalEmitted)
        }
        @Test
        fun `equipping an armor should update current armor and emit signal`() {
            val shield = EquippableItem.Armor(
                name = "Shield",
                stats = Stats(),
            )
            var signalEmitted = false
            inventory.onEquip.connect { item ->
                signalEmitted = true
                assert(item == shield)
            }

            inventory.armors.add(shield)
            inventory.equipItem(shield)

            assert(inventory.equipment.currArmor == shield)
            assert(signalEmitted)
        }
        @Test
        fun `unequipping an accessory should clear current accessory and emit signal`() {
            val ring = EquippableItem.Accessory(
                name = "Ring",
                stats = Stats(),
            )
            inventory.accessories.add(ring)
            inventory.equipItem(ring)

            var signalEmitted = false
            inventory.onUnequip.connect { item ->
                signalEmitted = true
                assert(item == ring)
            }

            inventory.unequipItem<EquippableItem.Accessory>()

            assert(inventory.equipment.currAccessory == null)
            assert(signalEmitted)
        }

        @Test
        fun `equipping an item not in inventory should throw an illegal state exception`() {
            val axe = EquippableItem.Weapon(
                name = "Axe",
                stats = Stats(),
            )
            assertThrows<IllegalStateException> { inventory.equipItem(axe) }
        }

        @Test
        fun `unequipping an item when none is equipped should throw an illegal state exception`() {
            assertThrows<IllegalStateException> { inventory.unequipItem<EquippableItem.Weapon>() }
        }
    }
}

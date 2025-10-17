package anchors.rogue.features.logbook

import anchors.rogue.ecs.managers.Manager
import anchors.rogue.features.logbook.bestiary.Bestiary
import anchors.rogue.features.logbook.inventory.Inventory
import anchors.rogue.features.logbook.inventory.InventoryData
import anchors.rogue.features.logbook.journal.Journal

class LogBookManager : Manager() {

    /**
     * Player's inventory containing items, equipment, and gold.
     */
    val inventory: Inventory by lazy {
        Inventory()
    }

    /**
     * Player's journal containing quests, notes, and lore entries.
     */
    val journal: Journal by lazy {
        Journal()
    }

    /**
     * Player's bestiary containing information about encountered creatures.
     */
    val bestiary: Bestiary by lazy {
        Bestiary()
    }

    /**
     * Reads the logbook data from player save file and initializes its components.
     */
    override fun setup() {
        super.setup()

        // TODO: Load logbook data from save file if available

        val inventoryData = /* load from save file or create new */ InventoryData()
        inventory.loadData(inventoryData)

    }

    override fun teardown() {
        super.teardown()
        //TODO("Not yet implemented")
    }
}

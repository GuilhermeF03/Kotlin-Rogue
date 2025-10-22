package anchors.rogue.features.logbook

import anchors.rogue.ecs.managers.Manager
import anchors.rogue.features.logbook.bestiary.Bestiary
import anchors.rogue.features.logbook.inventory.Inventory
import anchors.rogue.features.logbook.journal.Journal

class LogbookManager : Manager() {

    /**
     * Player's inventory containing items, equipment, and gold.
     */
    val inventory = Inventory()
    val bestiary = Bestiary()
    val journal = Journal()

    override fun setup() {}
    override fun teardown() {}
}

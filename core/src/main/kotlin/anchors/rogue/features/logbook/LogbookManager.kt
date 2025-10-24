package anchors.rogue.features.logbook

import anchors.rogue.features.logbook.bestiary.data.Bestiary
import anchors.rogue.features.logbook.inventory.data.Inventory
import anchors.rogue.features.logbook.journal.data.Journal
import anchors.rogue.shared.ecs.managers.Manager

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

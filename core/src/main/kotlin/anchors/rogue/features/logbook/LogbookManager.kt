package anchors.rogue.features.logbook

import anchors.rogue.features.logbook.bestiary.data.Bestiary
import anchors.rogue.features.logbook.inventory.data.Inventory
import anchors.rogue.features.logbook.journal.data.Journal
import anchors.rogue.shared.ecs.managers.Manager

/**
 * The **Logbook Manager** represents info about the player that isn't directly related to location, run info, or stats.
 *
 * Comprises 3 components:
 * * Inventory - holds all the player's items, loot, and equipment(weapons, armors, etc...)
 * * Bestiary - registers info about discovered enemies
 * * Journal - registers info about the world, such as areas and NPCs
 */
class LogbookManager : Manager() {
    val inventory = Inventory()
    val bestiary = Bestiary()
    val journal = Journal()

    override fun setup() {}

    override fun teardown() {}
}

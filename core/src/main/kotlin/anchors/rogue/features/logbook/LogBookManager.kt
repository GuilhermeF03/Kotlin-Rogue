package anchors.rogue.features.logbook

import anchors.rogue.ecs.managers.Manager
import anchors.rogue.features.logbook.bestiary.Bestiary
import anchors.rogue.features.logbook.inventory.Inventory
import anchors.rogue.features.logbook.journal.Journal

class LogBookManager private constructor(
    val inventoryManager: Inventory = Inventory(),
    val bestiary : Bestiary = Bestiary(),
    val journal : Journal = Journal(),
): Manager() {
    constructor() : this(Inventory(), Bestiary(), Journal())
    /**
     * Reads the logbook data from player save file.
     */
    override fun setup() {
        super.setup()
        //TODO("Not yet implemented")
    }

    override fun teardown() {
        super.teardown()
        //TODO("Not yet implemented")
    }
}

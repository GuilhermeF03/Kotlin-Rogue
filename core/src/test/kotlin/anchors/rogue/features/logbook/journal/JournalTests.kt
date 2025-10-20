package anchors.rogue.features.logbook.journal

import anchors.rogue.features.logbook.bestiary.Bestiary
import anchors.rogue.features.logbook.bestiary.BestiaryEntry
import anchors.rogue.utils.data.registry.IdRegistry
import kotlin.test.Test
import kotlin.test.assertEquals

class JournalTests {
    val bestiary = Journal { IdRegistry() }

    @Test
    fun `should add entry and notify`(){
        bestiary.addEntry(JournalEntry("dog"))
        assertEquals(1, bestiary.entries.size)
    }
}

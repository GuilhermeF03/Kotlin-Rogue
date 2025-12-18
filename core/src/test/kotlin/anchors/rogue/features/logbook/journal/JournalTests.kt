package anchors.rogue.features.logbook.journal

import anchors.framework.data.registry.IdRegistry
import anchors.rogue.features.logbook.journal.data.Journal
import anchors.rogue.features.logbook.journal.data.JournalEntry
import kotlin.test.Test
import kotlin.test.assertEquals

class JournalTests {
    val bestiary = Journal(IdRegistry())

    @Test
    fun `should add entry and notify`() {
        bestiary.addEntry(JournalEntry("dog"))
        assertEquals(1, bestiary.entries.size)
    }
}

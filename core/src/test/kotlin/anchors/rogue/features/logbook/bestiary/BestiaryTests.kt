package anchors.rogue.features.logbook.bestiary

import anchors.rogue.utils.data.registry.IdRegistry
import kotlin.test.Test
import kotlin.test.assertEquals

class BestiaryTests {
    val bestiary = Bestiary { IdRegistry() }

    @Test
    fun `should add entry and notify`(){
        bestiary.addEntry(BestiaryEntry(name="dog"))
        assertEquals(1, bestiary.discovered.size)
    }
}

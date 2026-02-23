package anchors.rogue.features.logbook.bestiary

import kotlin.test.Test
import kotlin.test.assertEquals
import anchors.rogue.features.logbook.bestiary.data.Bestiary
import anchors.rogue.features.logbook.bestiary.data.BestiaryEntry
import io.canopy.engine.core.managers.ManagersRegistry
import io.canopy.engine.data.core.registry.IdRegistry
import io.canopy.engine.data.saving.SaveManager
import org.junit.jupiter.api.BeforeAll

class BestiaryTests {
    val bestiary by lazy { Bestiary(IdRegistry()) }

    companion object {
        val saveManager = SaveManager()

        @JvmStatic
        @BeforeAll
        fun setup() {
            ManagersRegistry.withScope {
                register(saveManager)
            }
        }
    }

    @Test
    fun `should add entry and notify`() {
        bestiary.addEntry(BestiaryEntry(name = "dog"))
        assertEquals(1, bestiary.discovered.size)
    }
}

package anchors.rogue.features.logbook.bestiary

import anchors.rogue.features.logbook.bestiary.data.Bestiary
import anchors.rogue.features.logbook.bestiary.data.BestiaryEntry
import anchors.rogue.features.saving.SaveManager
import anchors.rogue.shared.ecs.managers.ManagersRegistry
import anchors.rogue.shared.utils.data.registry.IdRegistry
import org.junit.jupiter.api.BeforeAll
import kotlin.test.Test
import kotlin.test.assertEquals

class BestiaryTests {
    val bestiary by lazy { Bestiary(IdRegistry()) }

    companion object {
        val saveManager = SaveManager()

        @JvmStatic
        @BeforeAll
        fun setup() {
            ManagersRegistry.register(saveManager)
        }
    }

    @Test
    fun `should add entry and notify`() {
        bestiary.addEntry(BestiaryEntry(name = "dog"))
        assertEquals(1, bestiary.discovered.size)
    }
}

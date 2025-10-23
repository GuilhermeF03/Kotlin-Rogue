package anchors.rogue.features.logbook.bestiary

import anchors.rogue.utils.data.registry.IdRegistry
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertContentEquals

class BestiaryRegistryTests {
    val entries =
        listOf(
            BestiaryEntry("dog"),
            BestiaryEntry("cat"),
            BestiaryEntry("parrot"),
        )

    val registry = IdRegistry<BestiaryEntry>()

    @Test
    fun `should be unable to find any item`() {
        // Assert
        assertAll(
            {
                assertThrows<IllegalArgumentException> {
                    registry.mapIds<BestiaryEntry>(entries.map { it.id })
                }
            },
        )
    }

    @Test
    fun `should correctly map items`() {
        // Setup
        registry.loadRegistry(entries)
        // Act
        val actualEntries: List<BestiaryEntry> = registry.mapIds(entries.map { it.id })
        assertContentEquals(entries, actualEntries)
    }
}

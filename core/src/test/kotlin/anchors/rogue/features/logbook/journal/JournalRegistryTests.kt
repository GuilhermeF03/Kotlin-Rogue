package anchors.rogue.features.logbook.journal

import anchors.rogue.utils.data.registry.IdRegistry
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertContentEquals

class JournalRegistryTests {

    val entries = listOf(
        JournalEntry("sea"),
        JournalEntry("castle"),
        JournalEntry("princess")
    )

    val registry = IdRegistry<JournalEntry>()

    @Test
    fun `should be unable to find any item`(){
        // Assert
        assertAll(
            { assertThrows<IllegalArgumentException> {
                registry.mapIds<JournalEntry>(entries.map { it .id })
            }},
        )
    }
    @Test
    fun `should correctly map items`(){
        // Setup
        registry.loadRegistry(entries)
        // Act
        val actualEntries : List<JournalEntry> = registry.mapIds(entries.map { it.id })
        assertContentEquals(entries, actualEntries)
    }
}

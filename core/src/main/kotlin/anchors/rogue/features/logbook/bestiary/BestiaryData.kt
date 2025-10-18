package anchors.rogue.features.logbook.bestiary

import anchors.rogue.utils.data.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class BestiaryData(
    @Serializable(with = UUIDSerializer::class)
    val discoveredEntities: List<UUID> = emptyList()
)

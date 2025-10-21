package anchors.rogue.features.logbook.bestiary

import anchors.rogue.features.logbook.inventory.InventoryData
import anchors.rogue.features.saving.SaveModule
import kotlinx.serialization.KSerializer

data class BestiarySaveModule(
    private val bestiary: Bestiary
): SaveModule<BestiaryData>() {
    override val id: String = "bestiary"
    override val serializer: KSerializer<BestiaryData> = BestiaryData.serializer()
    override fun save() = bestiary.asData()
}

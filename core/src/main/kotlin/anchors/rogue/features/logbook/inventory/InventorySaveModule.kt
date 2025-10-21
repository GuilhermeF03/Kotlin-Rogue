package anchors.rogue.features.logbook.inventory

import anchors.rogue.features.saving.SaveModule
import kotlinx.serialization.KSerializer

data class InventorySaveModule(
    private val manager : InventoryManager
) : SaveModule<InventoryData>() {
    override val serializer: KSerializer<InventoryData> = InventoryData.serializer()
    override val id: String = "inventory"
    override fun save() = manager.asData()
}

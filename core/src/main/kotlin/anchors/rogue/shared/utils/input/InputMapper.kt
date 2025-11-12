package anchors.rogue.shared.utils.input

import anchors.rogue.shared.utils.data.registry.IdRegistry
import anchors.rogue.shared.utils.saving.registerSaveModule

class InputMapper {
    internal val mappings : MutableMap<String, MutableList<InputBind>> = mutableMapOf()

    init {
        registerSaveModule(
            "input",
            InputData.serializer(),
            onSave = { this.asData() },
            onLoad = ::loadData
        )
    }

    fun loadData(data : InputData){
        mappings.clear()
        mappings.putAll(data.mappings.associate { it.name to it.binds.toMutableList() })
    }

    fun mapToAction(bind : InputBind) : List<String>{
        return mappings.filterValues { bind in it }.keys.toList()
    }
}

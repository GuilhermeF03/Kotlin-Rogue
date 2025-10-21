package anchors.rogue.features.saving

import anchors.rogue.ecs.managers.ManagersRegistry
import anchors.rogue.utils.signals.createSignal
import kotlinx.serialization.Serializable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.Json

abstract class SaveModule<T : @Serializable Any> {
    abstract val id: String
    abstract val serializer: KSerializer<T>
    val onLoad = createSignal<T>()

    init {
        ManagersRegistry.get(SaveManager::class).register(this)
    }

    abstract fun save(): T
    fun loadFromJson(element: JsonElement) {
        onLoad.emit(Json.decodeFromJsonElement(serializer, element))
    }
}


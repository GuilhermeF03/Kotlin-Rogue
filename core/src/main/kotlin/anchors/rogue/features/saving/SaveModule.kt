package anchors.rogue.features.saving

import anchors.rogue.ecs.managers.ManagersRegistry
import anchors.rogue.utils.signals.createSignal
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

interface  SaveModule<T : @Serializable Any> {
     val id: String
     val serializer: KSerializer<T>

     val onSaveCallback: () -> T
    val onLoadCallback : (T) -> Unit get() = {}

    val onLoad get() = createSignal<T>().also { it connect onLoadCallback }

    fun save(): T = onSaveCallback()
    fun loadFromJson(element: JsonElement) {
        onLoad.emit(Json.decodeFromJsonElement(serializer, element))
    }
}

fun <T : @Serializable Any> registerSaveModule(
    id: String,
    serializer: KSerializer<T>,
    onSave: () -> T,
    onLoad: (T) -> Unit
) {
    val saveModule = object : SaveModule<T>{
        override val id = id
        override val serializer = serializer
        override val onSaveCallback = onSave
        override val onLoadCallback = onLoad
    }
    ManagersRegistry.get(SaveManager::class).register(saveModule)
}



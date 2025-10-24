package anchors.rogue.features.saving

import anchors.rogue.shared.ecs.managers.ManagersRegistry
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

interface SaveModule<T : @Serializable Any> {
    val id: String
    val serializer: KSerializer<T>
    val onSave: () -> T
    val onLoad: (T) -> Unit
}

fun <T : @Serializable Any> registerSaveModule(
    id: String,
    serializer: KSerializer<T>,
    onSave: () -> T,
    onLoad: (T) -> Unit = {},
) {
    val saveModule =
        object : SaveModule<T> {
            override val id = id
            override val serializer = serializer
            override val onSave = onSave
            override val onLoad = onLoad
        }
    ManagersRegistry.get(SaveManager::class).register(saveModule)
}

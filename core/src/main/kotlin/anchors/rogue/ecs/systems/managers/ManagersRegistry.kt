package anchors.rogue.ecs.systems.managers

import anchors.rogue.utils.log.info
import kotlin.reflect.KClass


/**
 * ManagersPipeline is responsible for managing a collection of Manager instances.
 * It allows registering managers and setting them up collectively.
 * Managers must be registered during the application initialization phase to ensure proper setup.
 */
object ManagersRegistry {
    private val managers = mutableMapOf<KClass<out Manager>, Manager>()

    fun <T : Manager> register(manager: T) {
        managers[manager::class] = manager
    }


    @Suppress("UNCHECKED_CAST")
    fun <T : Manager> get(clazz: KClass<T>) = managers.getValue(clazz) as T

    fun setup() {
        info("Bootstrapping managers[registered: ${managers.size}]...")
        managers.values.forEach { it.setup() }
    }

    fun teardown() {
        info("Tearing down managers[registered: ${managers.size}]...")
        managers.values.forEach { it.teardown() }
    }
}



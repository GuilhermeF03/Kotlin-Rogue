package anchors.rogue.misc

import anchors.rogue.utils.log.info


/**
 * ManagersPipeline is responsible for managing a collection of Manager instances.
 * It allows registering managers and setting them up collectively.
 * Managers must be registered during the application initialization phase to ensure proper setup.
 */
object ManagersPipeline{
    val managers: MutableList<Manager> = mutableListOf()

    fun registerManager(manager: Manager) {
        info("Registering manager: ${manager::class.simpleName}")
        managers += manager
    }

    fun setup() {
        info("Bootstrapping managers[registered: ${managers.size}]...")
        managers.forEach { it.setup() }
    }

    fun teardown() {
        info("Tearing down managers[registered: ${managers.size}]...")
        managers.forEach { it.teardown() }
    }

    inline fun<reified T : Manager> getManager() : T = managers
        .filterIsInstance<T>()
        .firstOrNull() ?: throw IllegalStateException("Manager of type ${T::class} not registered")
}



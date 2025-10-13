package anchors.rogue.utils.log


fun Any.info(msg: String) = ktx.log.info{"""[${this::class.simpleName ?: "Unknown"}] $msg"""}

fun Any.error(msg: String) = ktx.log.error{"""[${this::class.simpleName ?: "Unknown"}] $msg"""}

fun Any.debug(msg: String) = ktx.log.debug{"""[${this::class.simpleName ?: "Unknown"}] $msg"""}


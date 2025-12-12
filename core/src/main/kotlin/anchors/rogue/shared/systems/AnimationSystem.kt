package anchors.rogue.shared.systems

import anchors.rogue.shared.utils.misc.interpolate
import anchors.rogue.shared.utils.nodes.core.GlobalNodeSystem
import anchors.rogue.shared.utils.nodes.core.Node
import anchors.rogue.shared.utils.nodes.core.UpdatePhase
import anchors.rogue.shared.utils.nodes.types.visual.animation.AnimationPlayer
import anchors.rogue.shared.utils.nodes.types.visual.animation.tracks.ActionTrack
import anchors.rogue.shared.utils.nodes.types.visual.animation.tracks.Key
import anchors.rogue.shared.utils.nodes.types.visual.animation.tracks.PropertyTrack
import ktx.log.logger

class AnimationSystem :
    GlobalNodeSystem(
        UpdatePhase.AnimationBeforeScene,
        AnimationPlayer::class,
    ) {
    private val logger = logger<AnimationSystem>()
    private val queuedUpdates = mutableListOf<() -> Unit>()

    override fun processNode(
        node: Node<*>,
        delta: Float,
    ) {
        val animPlayer = node as AnimationPlayer
        animPlayer.time += delta

        queuedUpdates += getQueuedUpdates(animPlayer, animPlayer.time, delta)
    }

    override fun afterProcess(delta: Float) {
        queuedUpdates.forEach { it() }
        queuedUpdates.clear()
    }

    fun getQueuedUpdates(
        player: AnimationPlayer,
        time: Float,
        delta: Float,
    ): List<() -> Unit> {
        val anim = player.currentAnimation ?: return emptyList()

        val updates =
            anim.tracks.mapNotNull { track ->
                when (track) {
                    is PropertyTrack<*> -> {
                        @Suppress("UNCHECKED_CAST")
                        val t = track as PropertyTrack<Any>

                        val keys: List<Key<Any>> = t.keys
                        if (keys.isEmpty()) return@mapNotNull null

                        // Find surrounding keys
                        val prevKey = keys.lastOrNull { it.time <= time } ?: keys.first()
                        val nextKey = keys.firstOrNull { it.time > time } ?: prevKey

                        val factor =
                            if (prevKey == nextKey) {
                                0f
                            } else {
                                (time - prevKey.time) / (nextKey.time - prevKey.time)
                            }

                        val value = prevKey.value.interpolate(nextKey.value, factor)
                        return@mapNotNull { t.property.set(value) }
                    }

                    is ActionTrack -> {
                        val keys: List<Key<() -> Unit>> = track.keys
                        val key =
                            keys
                                .filter { it.time in (time - delta)..time && !it.executed }
                                .minByOrNull { it.time }
                                ?: return@mapNotNull null

                        key.executed = true
                        return@mapNotNull { key.value() }
                    }

                    else -> throw IllegalStateException("Unknown track type: $track")
                }
            }

        // logger.info { "Time: $time, delta: $delta, updates: ${updates.size}" }

        return updates
    }
}

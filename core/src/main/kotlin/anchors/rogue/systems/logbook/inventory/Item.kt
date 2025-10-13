package anchors.rogue.systems.logbook.inventory

import anchors.rogue.entities.Stats

sealed interface Item {
    val name: String
    val stats: Stats
    val rank: Int

    data class Weapon(
        val damage: Int,
        val speed: Int,
        val range: Int,
        override val name: String,
        override val stats: Stats,
        override val rank: Int,
    ) : Item

    data class Armor(
        //val defense: Int,  <-- Isto vem dos stats acho que não é necessário
        // val slot: ArmorSlot, <- bot generated
        override val name: String,
        override val stats: Stats,
        override val rank: Int,
    ) : Item

    data class Accessory(
        val effect: String,
        override val name: String,
        override val stats: Stats,
        override val rank: Int,
    ) : Item

    data class Consumable( // Não sei se vamos conseguir meter logo um sistema de items em batalha mas podemos ver
        val effect: String,
        override val name: String,
        override val stats: Stats,
        override val rank: Int,
    ) : Item
}


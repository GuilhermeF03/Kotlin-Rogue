package anchors.rogue.features.logbook.inventory

import anchors.rogue.features.stats.data.Stats
import kotlinx.serialization.Serializable


/**
 * Represents an item in the game with various types such as Trinket, Weapon, Armor, Accessory, and Consumable.
 * Each item has a name, description, stats, and rank.
 * The different item types have their own specific attributes in addition to the common ones.
 */
@Serializable
sealed interface Item {
    val name: String
    val description : String
    val rank: ItemRank
    val sellValue : Int

    /**
     * Trinket items are special items that do not have stats but can be sold for gold.
     * They have a name, description, rank, and sell value.
     */
    @Serializable
    data class Trinket(
        override val name: String,
        override val description: String = "",
        override val rank: ItemRank = ItemRank.COMMON,
        override val sellValue: Int = 0, // Trinkets can be sold for gold
    ) : Item

    /**
     * Consumable items can be used to provide temporary effects or restore health/mana.
     * They also have a name, description, stats, and rank.
     */
    @Serializable
    data class Consumable( // Não sei se vamos conseguir meter logo um sistema de ‘items’ em batalha, mas podemos ver
        override val name: String,
        override val description: String = "",
        override val rank: ItemRank = ItemRank.COMMON,
        override val sellValue: Int = 0,
        val effect: String = "", // Descrição do efeito do consumível -≥ mudar para objeto Effect mais tarde
    ) : Item
}


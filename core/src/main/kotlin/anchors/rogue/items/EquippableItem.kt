package anchors.rogue.items

import anchors.rogue.features.stats.data.Stats
import kotlinx.serialization.Serializable


/**
 * Equippable items are items that can be equipped by the player to enhance their stats.
 * They have a name, description, stats, and rank.
 */
@Serializable
sealed interface EquippableItem : Item {
    val stats : Stats
    override val quantity: Int get() = 1
    /**
     * Weapon items are used for improving attack-based stats.
     * They also have a name, description, stats, and rank.
     */
    @Serializable
    data class Weapon(
        override val name: String,
        override val description: String = "",
        override val rank: ItemRank = ItemRank.COMMON,
        override val sellValue: Int = 0,
        override val stats: Stats = Stats(),
        //override val quantity: Int = 0,
        val minDamage: Int = 0,
        val maxDamage: Int = 0,
        val range: Int = 1, // Será que armas vão ter range? Por exemplo, espadas devem atacar inimigos próximos, mas arcos podem atacar de longe
    ) : EquippableItem {
        override val domain = "weapon"
    }
    /**
     * Armor items are used to improve defense-related stats.
     * They also have a name, description, stats, and rank.
     */
    @Serializable
    data class Armor(
        override val name: String,
        override val description: String = "",
        override val rank: ItemRank = ItemRank.COMMON,
        override val sellValue: Int = 0,
        override val stats: Stats = Stats(),
        //override val quantity: Int = 0
        //val defense: Int, ≤- Isto vem dos stats acho que não é necessário
    ) : EquippableItem {
        override val domain = "armor"
    }
    /**
     * Accessory items provide various effects or bonuses to the player.
     * They also have a name, description, stats, and rank.
     */
    @Serializable
    data class Accessory(
        override val name: String,
        override val description: String = "",
        override val rank: ItemRank = ItemRank.COMMON,
        override val sellValue: Int = 0,
        override val stats: Stats = Stats(),
        //override val quantity: Int = 0,
        val effect: String = "", // Descrição do efeito do acessório -≥ mudar para objeto Effect mais tarde
    ) : EquippableItem {
        override val domain = "accessory"
    }
}

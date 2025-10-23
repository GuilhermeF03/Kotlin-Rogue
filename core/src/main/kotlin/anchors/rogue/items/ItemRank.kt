package anchors.rogue.items

/**
 * ItemRank represents the rarity or quality of an item.
 * Each rank is associated with an integer value for comparison purposes.
 * - COMMON: Basic items with no special attributes.
 * - UNCOMMON: Slightly better than common items, may have minor enhancements.
 * - RARE: Items with significant enhancements or unique properties.
 * - EPIC: High-quality items with powerful attributes and effects.
 * - LEGENDARY: The best items available, often with unique abilities and the highest stats
 */
enum class ItemRank {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY,
}

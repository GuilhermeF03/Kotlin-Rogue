package anchors.rogue.entities

data class Stats(
    val vitality: Int,
    val strength: Int,
    val constitution: Int,
    val dexterity: Int,
    val luck: Int,
    val intelligence: Int,
) {
    override fun toString(): String = """
            VIT: $vitality
            STR: $strength
            CON: $constitution
            DEX: $dexterity
            LUCK: $luck
            INT: $intelligence
            """
}

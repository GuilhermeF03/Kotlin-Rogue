package anchors.rogue.systems.combat

data class Skill(
    val id: UUID,
    val name: String,
    val description: String = "",
    val type: SkillType,
    val category: SkillCategory,
    val effect: SkillEffect,
    val basePower: Int,
    val cost: Int
)

enum class SkillType {
    PHYSICAL,
    FIRE,
    ICE,
    LIGHTNING,
    POISON,
    HOLY
}

enum class SkillCategory {
    PHYSICAL,
    MAGIC,
    BUFF,
    DEBUFF
}

data class SkillEffect(
    val effect: EffectType,
    val multiplier: Double = 0.0,
    val duration: Int = 0
)

enum class EffectType {
    NONE,
    ATTACK_UP,
    DEFENSE_UP,
    SPEED_UP,
    CRIT_RATE_UP,
    EVADE_UP,
    ATTACK_DOWN,
    DEFENSE_DOWN,
    SPEED_DOWN,
    HEAL,
    REGENERATION,
    POISON,
    STUN,
    BURN,
    FREEZE,
    PARALYZE,
}

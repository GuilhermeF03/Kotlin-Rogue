package anchors.rogue.features.combat

import kotlinx.serialization.Serializable

@Serializable
data class Skill(
    val id: String,
    val name: String,
    val description: String = "",
    val type: SkillType,
    val category: SkillCategory,
    val effect: SkillEffect,
    val basePower: Int = 0,
    val cost: Int = 0,
)

@Serializable
enum class SkillType {
    PHYSICAL,
    FIRE,
    ICE,
    LIGHTNING,
    POISON,
    HOLY,
}

@Serializable
enum class SkillCategory {
    PHYSICAL,
    MAGIC,
    BUFF,
    DEBUFF,
}

@Serializable
data class SkillEffect(
    val type: EffectType,
    val multiplier: Double = 0.0,
    val duration: Int = 0,
)

@Serializable
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

package anchors.rogue.data.parsers

import anchors.rogue.entities.Race
import anchors.rogue.entities.Stats
import anchors.rogue.systems.combat.EffectType
import anchors.rogue.systems.combat.Skill
import anchors.rogue.systems.combat.SkillCategory
import anchors.rogue.systems.combat.SkillEffect
import anchors.rogue.systems.combat.SkillType
import org.junit.Test
import org.assertj.core.api.Assertions.assertThat
import java.util.UUID

class JsonParsingTests {
    val racesJson = """
      [{
        "id": "00000000-0000-0000-0000-000000000000",
        "name": "HUMAN",
        "description": "Versatile and ambitious...",
        "stats": {
          "vitality": 5,
          "strength": 5,
          "constitution": 5,
          "dexterity": 5,
          "luck": 5,
          "intelligence": 5
        },
        "blessing": {
            "id": "00000000-0000-0000-0000-000000000000",
          "type": "PHYSICAL",
          "category": "PHYSICAL",
          "name": "Jack Of All Trades",
          "description": "Adapt to any challenge...",
          "effect": {
            "type": "ATTACK_UP",
            "multiplier": 0.5,
            "duration": 1
          }
        },
        "curse": {
            "id": "00000000-0000-0000-0000-000000000000",
          "type": "PHYSICAL",
          "category": "PHYSICAL",
          "name": "Master Of None",
          "description": "You can’t level your character class past level 7",
          "effect": {
            "type": "ATTACK_UP",
            "multiplier": 0.5,
            "duration": 1
          }
        }
      }]
    """.trimIndent()

    @Test
    fun `should correctly parse json`() {
        // Setup
        val expectedRaces = listOf(Race(
            id = UUID.fromString("00000000-0000-0000-0000-000000000000"),
            name = "HUMAN",
            stats = Stats(5, 5, 5, 5, 5, 5),
            blessing = Skill(
                id = UUID.fromString("00000000-0000-0000-0000-000000000000"),
                name = "Jack Of All Trades",
                description = "Adapt to any challenge...",
                type = SkillType.PHYSICAL,
                category = SkillCategory.PHYSICAL,
                effect = SkillEffect(
                    type = EffectType.ATTACK_UP,
                    multiplier = 0.5,
                    duration = 1
                )
            ),
            curse = Skill(
                id = UUID.fromString("00000000-0000-0000-0000-000000000000"),
                name = "Master Of None",
                description = "You can’t level your character class past level 7",
                type = SkillType.PHYSICAL,
                category = SkillCategory.PHYSICAL,
                effect = SkillEffect(
                    type = EffectType.ATTACK_UP,
                    multiplier = 0.5,
                    duration = 1
                )
            ),
            description = "Versatile and ambitious..."
        ))
        // Act
        val races : List<Race> = JsonParser.parseString(racesJson)
        // Assert
        assertThat(races).usingRecursiveComparison().isEqualTo(expectedRaces)
    }
}

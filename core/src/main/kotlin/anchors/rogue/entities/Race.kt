package anchors.rogue.entities

import anchors.rogue.systems.combat.Skill

data class Race(
    val id: Int,
    val name: String,
    val stats: Stats,
    val blessing: Skill,
    val curse: Skill,
    val description: String
)

/*import com.moandjiezana.toml.Toml
import java.io.File

fun loadRacesFromToml(filePath: String): List<RaceData> {
    val toml = Toml().read(File(filePath))
    return toml.getTables("races").map { table ->
        RaceData(
            id = table.getLong("id").toInt(),
            name = table.getString("name"),
            vitals = Stats(
                vitality = table.getTable("vitals").getLong("vitality").toInt(),
                strength = table.getTable("vitals").getLong("strength").toInt(),
                constitution = table.getTable("vitals").getLong("constitution").toInt(),
                dexterity = table.getTable("vitals").getLong("dexterity").toInt(),
                luck = table.getTable("vitals").getLong("luck").toInt(),
                intelligence = table.getTable("vitals").getLong("intelligence").toInt()
            ),
            blessing = Skill(
                name = table.getTable("blessing").getString("name"),
                description = table.getTable("blessing").getString("description")
            ),
            curse = Skill(
                name = table.getTable("curse").getString("name"),
                description = table.getTable("curse").getString("description")
            ),
            lore = table.getString("lore")
        )
    }
}*/

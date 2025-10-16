package anchors.rogue.utils.data.parsers

import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.json.Json

/**
 * A utility object for parsing JSON data from files or strings into Kotlin objects.
 */
object JsonParser {
    /**
     * Parses a JSON file and converts its content into a list of objects of type [T].
     * @param T The type of objects to parse the JSON into.
     * @param filePath The path to the JSON file.
     * @return A list of objects of type [T] parsed from the JSON file.
     */
    inline fun <reified T> parseFile(file : FileHandle): List<T> =
        Json.decodeFromString(file.file().readText())
    /**
     * Parses a JSON string and converts its content into a list of objects of type [T].
     * @param T The type of objects to parse the JSON into.
     * @param jsonString The JSON string to parse.
     * @return A list of objects of type [T] parsed from the JSON string.
     */
    inline fun <reified T> parseString(jsonString: String): List<T> = Json.decodeFromString(jsonString)
}

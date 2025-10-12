package anchors.rogue.data.parsers

import kotlinx.serialization.json.Json
import java.io.File

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
    inline fun <reified T> parseFile(filePath: String): List<T> =
        Json.decodeFromString(File(filePath).readText())

    /**
     * Parses a JSON string and converts its content into a list of objects of type [T].
     * @param T The type of objects to parse the JSON into.
     * @param jsonString The JSON string to parse.
     * @return A list of objects of type [T] parsed from the JSON string.
     */
    inline fun <reified T> parseString(jsonString: String): List<T> = Json.decodeFromString(jsonString)
}

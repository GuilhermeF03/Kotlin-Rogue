# How to use the **JsonParser** object

## Simple usage

### 0. Define a  **\@‌Serializable class**

```kotlin
@Serializable
data class SimpleData(
    val id: Int,
    val name: String
)
```
### 1. Parse file

```kotlin
val file = Gdx.file.internal("file.json")

val data : SimpleData = JsonParser.parseFile(file)
```

### 2. Parse string

```kotlin
val string = """{"id": 0, "name": "abc"}"""

val data : SimpleData = JsonParser.parseString(string)
```
---
# Polymorphic class

Let´s suppose you have an **Item** class:

```kotlin
@Serializable
interface Item
```

And it has many multiple implementations.

```kotlin
@Serializable
@SerialName("Weapon") // NEEDED
data class Weapon(val damage : Int) : Item
```

```
@Serializable
@SerialName("Armor") // NEEDED
data class Armor(val damage : Int) : Item
```

Suppose in your json file you have somethin like this:

```json
{"items": [
  {"type": "Weapon", "damage": "10"},
  {"type": "Armor", "protection": "5"}
]}
```

And, on parsing, you want a list with both those items, you can do so.

### 0. Tell the parser this is a polymorphic parsing

```kotlin
val module = SerializersModule {
    polymorphic(Item::class) {
        subclass(Weapon::class)
        subclass(Armor::class)
    }
}
```

### 1.  Pass module into parsing functions

```kotlin
 val parsedList : List<Item> = JsonParser.parseString(jsonString, module)
```

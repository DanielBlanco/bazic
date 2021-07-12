package app.graphql

case class Character(name: String, age: Int)

// schema
case class CharacterName(name: String)
case class Queries(
    characters: List[Character],
    character: CharacterName => Option[Character]
)

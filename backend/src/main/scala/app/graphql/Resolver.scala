package app.graphql


def getCharacters: List[Character] = List()

def getCharacter(name: String): Option[Character] = None

// Queries --> Read only.
val queries = Queries(
  getCharacters,
  args => getCharacter(args.name)
)

// Mutations --> Read/Write.

// Subscriptions --> Websockets.

package app.server

import java.io.File
import zio.*
import zio.config.*
import zio.config.syntax.*
import zio.config.ConfigDescriptor.*
// import zio.config.typesafe.*

final case class Config(server: ServerConfig, db: DBConfig)
final case class DBConfig(uri: String)
final case class ServerConfig(host: String, port: Int) 

/** @todo Fix NullPointerException on zio config. */
object Config:
  type Dep = Has[Config]

  val live: ZLayer[system.System, Nothing, Dep] =
    ZLayer.succeed(
      Config(ServerConfig("0.0.0.0", 9999), DBConfig("xx"))
    )
    // TypesafeConfig.fromDefaultLoader(descriptor).orDie

  val service: URIO[Dep, Config] =
    ZIO.service[Config]

  private val db: ConfigDescriptor[DBConfig] =
    (
      string("uri")
    )(DBConfig.apply, c => Some(c.uri))

  private val server: ConfigDescriptor[ServerConfig] =
    (
      string("host").default("0.0.0.0") |@|
        int("port").default(9000)
    )(ServerConfig.apply, c => Some((c.host, c.port)))

  private val descriptor: ConfigDescriptor[Config] =
    (
      nested("server")(server) |@| nested("db")(db)
    )(Config.apply, c => Some((c.server, c.db)))


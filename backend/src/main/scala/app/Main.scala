package app

import app.server.*
import zio.*
import zio.blocking.*
import zio.clock.*
import zio.console.*
import zhttp.http._
import zhttp.service.Server

object Main extends App:

  type AppEnv =
    Blocking
      with Clock
      with Console
      with Config.Dep

  val live =
    Blocking.live ++
    Clock.live ++
    Console.live ++
    Config.live

  override def run(args: List[String]): URIO[ZEnv, ExitCode] =
    program.provideSomeLayer[ZEnv](live).exitCode

  private def program: RIO[AppEnv, Unit] =
    for
      _ <- putStrLn("Starting the application")
      c <- Config.service
      _ <- putStrLn(s"Server host: ${c.server.host}")
      _ <- putStrLn(s"Server port: ${c.server.port}")
      p <- buildServer(c.server)
    yield
      p

  private def buildServer(cfg: ServerConfig) =
    Server.start(cfg.port, httpApp).map(_ => ())

  private val httpApp =
    Http.collect[Request] {
      case Method.GET -> Root / "text" => Response.text("Hello World!")
    }

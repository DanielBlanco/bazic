package app

import app.server.*
import app.graphql.*
import caliban.*
import caliban.GraphQL.graphQL
import caliban.RootResolver
import zio.*
import zio.stream.ZStream
import zio.blocking.*
import zio.clock.*
import zio.console.*
import zio.prelude.*
import zhttp.http.*
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
      _ <- putStrLn("---- GraphQL ----")
      _ <- putStrLn(api.render)
      _ <- putStrLn("-----------------")
      _ <- Server.start(c.server.port, httpApp)
    yield
      ()

  private lazy val httpApp = healtcheckRoutes +++ graphqlRoutes

  private lazy val healtcheckRoutes =
    Http.collect[Request] {
      case Method.GET -> Root / "hello" => Response.text("Hello World!")
    }

  private lazy val graphqlRoutes =
    Http.route[Request] {
      case _ -> Root / "api" / "graphql" =>
        Http.fromEffect {
          for
            interpreter <- api.interpreter
          yield ZHttpAdapter.makeHttpService(interpreter)
        }.flatten
      // case _ -> Root / "graphiql"        =>
      //   graphiql
    }

  val api = graphQL(RootResolver(queries))

  // private val graphiql =
  //   Http.succeed(
  //     Response.http(
  //       content = HttpData.fromStream(ZStream.fromResource("graphiql.html"))
  //     )
  //   )

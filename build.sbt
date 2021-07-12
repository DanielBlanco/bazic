import Dependencies._

val scala3Version = "3.0.0"

lazy val commonSettings = Seq(
  name                 := "bazic",
  version              := "0.1.0",
  organization         := "com.innovabot",
  organizationName     := "innovabot",
  scalaVersion         := scala3Version,
  scalacOptions       ++= Seq(
    "-Xignore-scala2-macros",
    "-Xfatal-warnings",
    "-deprecation",
    "-feature",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-language:higherKinds",
    "-Ykind-projector"
  ),
  resolvers           ++= Resolvers.sonatype,
  libraryDependencies ++= Testing.shared.value,
  libraryDependencies ++= Http.shared.value,
  libraryDependencies ++= ZIO.shared.value,
  testFrameworks       += Testing.framework,
  Test / fork          := true
)

lazy val backend = project
  .in(file("backend"))
  .enablePlugins(JavaAppPackaging)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Http.backend.value,
    libraryDependencies ++= ZIO.backend.value
  )
  .dependsOn(shared)

lazy val frontend = project
  .in(file("frontend"))
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings)
  .settings(
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) },
    scalaJSLinkerConfig ~= { _.withSourceMap(false) },
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies            ++= Laminar.frontend.value
  )
  .dependsOn(shared)

lazy val shared = project
  .enablePlugins(ScalaJSPlugin)
  .in(file("shared"))
  .settings(commonSettings)
  .settings(
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) },
    scalaJSLinkerConfig ~= { _.withSourceMap(false) }
  )

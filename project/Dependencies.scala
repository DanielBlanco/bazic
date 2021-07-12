import sbt._
import sbt.Keys._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import CrossVersion._

object Dependencies {
  object V {
    val caliban    = "1.1.0"
    val laminar    = "0.13.0"
    val laminext   = "0.13.5"
    val sttp       = "3.3.6"
    val zioConfig  = "1.0.6"
    val zioHttp    = "1.0.0.0-RC17"
    val zioPrelude = "1.0.0-RC5"
    val zio        = "1.0.9"
  }

  object Laminar {
    def frontend = Def.setting(
      Seq[ModuleID](
        "com.raquo"   %%% "laminar"   % V.laminar,
        "io.laminext" %%% "websocket" % V.laminext
      )
    )
  }

  object Http {
    def shared = Def.setting(
      Seq[ModuleID](
        "com.softwaremill.sttp.client3" %%% "core" % V.sttp
      )
    )

    def backend = Def.setting(
      Seq[ModuleID](
        "com.github.ghostdogpr"         %% "caliban"                % V.caliban,
        "com.github.ghostdogpr"         %% "caliban-zio-http"       % V.caliban,
        "io.d11"                        %% "zhttp"                  % V.zioHttp,
        "com.softwaremill.sttp.client3" %% "httpclient-backend-zio" % V.sttp
      )
    )
  }

  object ZIO {
    def shared = Def.setting(
      Seq[ModuleID](
        "dev.zio" %%% "zio"         % V.zio,
        "dev.zio" %%% "zio-streams" % V.zio
      )
    )

    def backend = Def.setting(
      Seq[ModuleID](
        "dev.zio" %% "zio-prelude"         % V.zioPrelude,
        "dev.zio" %% "zio-config"          % V.zioConfig,
        "dev.zio" %% "zio-config-yaml"     % V.zioConfig,
        "dev.zio" %% "zio-config-typesafe" % V.zioConfig
      )
    )
  }

  object Testing {
    val shared = Def.setting(
      Seq[ModuleID](
        "dev.zio" %%% s"zio-test"     % V.zio % Test,
        "dev.zio" %%% s"zio-test-sbt" % V.zio % Test
      )
    )

    val framework = new TestFramework("zio.test.sbt.ZTestFramework")
  }
}

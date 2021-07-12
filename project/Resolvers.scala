import sbt._

object Resolvers {

  val sonatype = Seq(
    Resolver.sonatypeRepo("snapshots"),
    "Sonatype OSS Snapshots s01" at "https://s01.oss.sonatype.org/content/repositories/snapshots"
  )
}

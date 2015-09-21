
val buildScalaVersion = "2.11.7"
val scalajsReactVersion = "0.9.2"

enablePlugins(ScalaJSPlugin)

organization := "io.github.widok"
name := "widok-scalajs-react-wrapper"
version := "0.1.0-SNAPSHOT"
scalaVersion := buildScalaVersion

libraryDependencies ++= Seq(
  "io.github.widok" %%% "widok" % "0.2.2" withSources() withJavadoc(),
  "com.github.japgolly.scalajs-react" %%% "core" % scalajsReactVersion
)

publishTo := Some("releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2")
pomExtra :=
  <url>https://github.com/mkotsbak/widok-scalajs-react-wrapper</url>
    <licenses>
      <license>
        <name>Apache-2.0</name>
        <url>https://www.apache.org/licenses/LICENSE-2.0.html</url>
      </license>
    </licenses>
    <scm>
      <url>git://github.com/mkotsbak/widok-scalajs-react-wrapper.git</url>
    </scm>
    <developers>
      <developer>
        <id>mkotsbak</id>
        <name>Marius B. Kotsbak</name>
        <url>https://github.com/mkotsbak</url>
      </developer>
    </developers>

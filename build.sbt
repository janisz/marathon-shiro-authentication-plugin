name := "marathon-shiro-authentication-plugin"

version := "1.0"

scalaVersion := "2.11.7"

resolvers ++= Seq(
"Mesosphere Public Repo" at "http://downloads.mesosphere.io/maven",
Classpaths.sbtPluginReleases
)

libraryDependencies ++= Seq(
"mesosphere.marathon" %% "plugin-interface" % "0.12.0-SNAPSHOT" % "provided",
"com.typesafe.play" % "play-json_2.11" % "2.3.9" % "provided",
"log4j" % "log4j" % "1.2.17" % "provided",
"org.apache.shiro" % "shiro-core" % "1.2.4",
"org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)

publishTo := Some(Resolver.file("file",  new File( "releases" )) )
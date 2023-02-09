ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.10"

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val baseName = "clean-arch-example"
lazy val scala2_13 = "2.13.10"

lazy val akkaVersion         = "2.7.0"
lazy val akkaHttpVersion     = "10.4.0"
lazy val akkaHttpCorsVersion = "1.1.3"
lazy val catsVersion         = "2.9.0"
lazy val catsEffectVersion   = "3.4.6"
lazy val doobieVersion       = "1.0.0-RC2"
lazy val airframeVersion     = "23.1.1"
lazy val scalaTestVersion    = "3.2.15"
lazy val javaJwtVersion      = "4.2.2"

lazy val `tools` = (project in file("tools")).settings(
  name := s"$baseName-tools",
  scalaVersion := scala2_13,
  libraryDependencies ++= Seq()
)

lazy val `domain` = (project in file("modules/domain")).settings(
  name := s"$baseName-domain",
  assemblySettings,
  scalaVersion := scala2_13,
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % catsVersion,
    "org.scalatest" %% "scalatest" % scalaTestVersion
  )
)

lazy val `usecases` = (project in file("modules/usecases")).settings(
  name := s"$baseName-usecases",
  assemblySettings,
  scalaVersion := scala2_13,
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % catsVersion,
    "org.scalatest" %% "scalatest" % scalaTestVersion
  )
).dependsOn(`domain`)

lazy val `interfaces` = (project in file("modules/interfaces")).settings(
  name := s"$baseName-interfaces",
  assemblySettings,
  scalaVersion := scala2_13,
  libraryDependencies ++= Seq(
    "org.typelevel"      %% "cats-core"            % catsVersion,
    "org.typelevel"      %% "cats-effect"          % catsEffectVersion,
    "org.scalatest"      %% "scalatest"            % scalaTestVersion,

    "com.typesafe.akka"  %% "akka-actor"           % akkaVersion,
    "com.typesafe.akka"  %% "akka-slf4j"           % akkaVersion,
    "com.typesafe.akka"  %% "akka-stream"          % akkaVersion,
    "com.typesafe.akka"  %% "akka-http"            % akkaHttpVersion,
    "com.typesafe.akka"  %% "akka-http-spray-json" % akkaHttpVersion,
    "com.typesafe.akka"  %% "akka-stream-testkit"  % akkaVersion,
    "com.typesafe.akka"  %% "akka-http-testkit"    % akkaHttpVersion,
    "ch.megard"          %% "akka-http-cors"       % akkaHttpCorsVersion,

    "org.wvlet.airframe" %% "airframe"             % airframeVersion,

    "com.auth0"          % "java-jwt"              % javaJwtVersion,

    "org.tpolecat"      %% "doobie-core"           % doobieVersion,
    "org.tpolecat"      %% "doobie-postgres"       % doobieVersion
  )
).dependsOn(`usecases`)

lazy val `api-server` = (project in file("api-server")).settings(
  name := s"$baseName-api-server",
  assemblySettings,
  scalaVersion := scala2_13,
  libraryDependencies ++= Seq(
  )
).dependsOn(`interfaces`)

lazy val root = (project in file("."))
  .settings(
    name := baseName,
    assemblySettings,
  )
  .aggregate(`tools`, `domain`, `usecases`, `interfaces`, `api-server`)
  .dependsOn(`api-server`)

lazy val assemblySettings = Seq(
  assemblyJarName := name.value + ".jar",
  assembly / assemblyMergeStrategy := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case PathList("application.conf")  => MergeStrategy.concat
    case _                             => MergeStrategy.first
  }
)

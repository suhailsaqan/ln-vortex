import sbt.Keys.excludeLintKeys

import scala.util.Properties

val scala2_12 = "2.12.15"
val scala2_13 = "2.13.8"

ThisBuild / scalafmtOnCompile := !Properties.envOrNone("CI").contains("true")

ThisBuild / scalaVersion := scala2_13

ThisBuild / crossScalaVersions := List(scala2_13, scala2_12)

//https://github.com/sbt/sbt/pull/5153
//https://github.com/bitcoin-s/bitcoin-s/pull/2194
Global / excludeLintKeys ++= Set(
  com.typesafe.sbt.packager.Keys.maintainer,
  Keys.mainClass
)

//needed so that we can use our versions with docker
//see: https://github.com/dwijnand/sbt-dynver#portable-version-strings
//https://github.com/bitcoin-s/bitcoin-s/issues/2672
ThisBuild / dynverSeparator := "-"

//don't require the leading 'v' on dynver versioning
//as that doesn't work with windows or mac versioning
ThisBuild / dynverVTagPrefix := false

name := """knapsack-optimizer-service"""
organization := "is.sveinbjorn"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "org.projectlombok" % "lombok" % "1.18.6" % "provided"
libraryDependencies += "io.vavr" % "vavr" % "0.9.3"
libraryDependencies += "com.google.guava" % "guava" % "19.0"

libraryDependencies += "com.typesafe.play" %% "play-native-loader" % "1.0.0"




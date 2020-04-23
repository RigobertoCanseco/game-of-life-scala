name := "game-of-life-scala"
//version := "0.1"
//scalaVersion := "2.12.7"
//libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.1.1"

lazy val buildSettings = Seq(
    version := "0.0.1-SNAPSHOT",
    organization := "com.rigobertocanseco",
    scalaVersion := "2.12.7",
    scalacOptions := Seq("-deprecation", "-unchecked"),
    resolvers += Resolver.sonatypeRepo("public")
)

lazy val swingDependencies = Def.setting {
    "org.scala-lang" % "scala-swing" % scalaVersion.value
}

lazy val root = (project in file(".")).
    settings(buildSettings: _*).
    settings(name := "game-of-life-scala")

lazy val library = (project in file("library")).
    settings(buildSettings: _*)

lazy val swing = (project in file("swing")).
    settings(buildSettings: _*).
    settings(
        fork in run := true,
        libraryDependencies += swingDependencies.value
    ).
    dependsOn(library)

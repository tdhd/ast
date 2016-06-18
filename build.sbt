scalaVersion := "2.11.8"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"
libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.11.8"
libraryDependencies += "org.scalameta" %% "scalameta" % "1.0.0"

libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.3.0"
libraryDependencies += "org.scala-lang.modules" %% "scala-pickling" % "0.10.1"

//lazy val root = Project("root", file(".")) dependsOn ast
//lazy val ast = RootProject(uri("git://github.com/tdhd/ast.git#v0.1.0"))

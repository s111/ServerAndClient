name := "Server"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "org.apache.tika" % "tika-app" % "1.4"
)     

play.Project.playJavaSettings

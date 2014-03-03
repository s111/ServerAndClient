name := "Server"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "net.coobird" % "thumbnailator" % "0.4.7",
  "commons-io" % "commons-io" % "2.4"
)     

play.Project.playJavaSettings

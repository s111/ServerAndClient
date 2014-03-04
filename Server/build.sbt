name := "Server"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "org.apache.commons" % "commons-imaging" % "1.0-SNAPSHOT",
  "net.coobird" % "thumbnailator" % "0.4.7",
  "commons-io" % "commons-io" % "2.4"
)     

resolvers += "apache.snapshots" at "http://repository.apache.org/snapshots"

play.Project.playJavaSettings

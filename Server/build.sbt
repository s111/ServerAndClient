name := "Server"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "org.apache.commons" % "commons-imaging" % "1.0-SNAPSHOT"
)

resolvers += "apache.snapshots" at "http://repository.apache.org/snapshots"

play.Project.playJavaSettings
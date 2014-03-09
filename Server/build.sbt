name := "Server"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "org.apache.commons" % "commons-imaging" % "1.0-SNAPSHOT",
  "net.coobird" % "thumbnailator" % "0.4.7",
  "commons-io" % "commons-io" % "2.4",
  "org.mockito" % "mockito-all" % "1.9.5",
  "org.apache.tika" % "tika-app" % "1.5" excludeAll(ExclusionRule(organization = "org.slf4j"))
)

resolvers += "apache.snapshots" at "http://repository.apache.org/snapshots"

play.Project.playJavaSettings

net.virtualvoid.sbt.graph.Plugin.graphSettings

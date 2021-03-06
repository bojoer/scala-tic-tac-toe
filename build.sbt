seq(com.typesafe.sbt.SbtStartScript.startScriptForClassesSettings: _*)

name := "TicTacToe"

version := "1.0"

scalaVersion := "2.10.3"

mainClass in Compile := Some("com.markdessain.Server")

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test"

resolvers += "spray" at "http://repo.spray.io/"

resolvers += Classpaths.typesafeResolver

libraryDependencies += "io.spray" %%  "spray-json" % "1.2.5"

libraryDependencies ++= {
  val akkaV = "2.3.0"
  val sprayV = "1.3.1"
  Seq(
    "io.spray"            %   "spray-routing" % sprayV,
    "io.spray"            %   "spray-can" % sprayV,
    "org.eclipse.jetty"   %   "jetty-webapp"  % "9.1.0.v20131115" % "container",
    "org.eclipse.jetty"   %   "jetty-plus"    % "9.1.0.v20131115" % "container",
    "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container"  artifacts Artifact("javax.servlet", "jar", "jar"),
    "io.spray"            %   "spray-testkit" % sprayV % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV % "test",
    "org.specs2"          %%  "specs2"        % "2.2.3" % "test"
  )
}

seq(webSettings: _*)

assemblySettings

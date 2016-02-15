name := """InfostroyFinalTask"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, QueryDSLPlugin)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJpa.exclude("org.hibernate.javax.persistence", "hibernate-jpa-2.0-api"),
  javaJdbc,
  cache,
  javaWs
)

libraryDependencies += "org.postgresql" % "postgresql" % "9.4.1207"

libraryDependencies += "org.hibernate" % "hibernate-entitymanager" % "4.3.9.Final"

libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.4"

libraryDependencies += "commons-collections" % "commons-collections" % "3.0"

libraryDependencies += filters

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

EclipseKeys.preTasks := Seq(compile in Compile)

PlayKeys.externalizeResources := false

fork in run := true
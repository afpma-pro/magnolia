import com.softwaremill.UpdateVersionInDocs
import com.softwaremill.SbtSoftwareMillCommon.commonSmlBuildSettings
import com.softwaremill.Publish.{updateDocs, ossPublishSettings}

val scala3 = "3.3.5"

ThisBuild / dynverTagPrefix := "scala3-v" // a custom prefix is needed to differentiate tags between scala2 & scala3 versions

val commonSettings = commonSmlBuildSettings ++ ossPublishSettings ++ Seq(
  scalaVersion := scala3,
  organization := "pro.afpma",
  description := "Fast, easy and transparent typeclass derivation for Scala 3",
  updateDocs := UpdateVersionInDocs(
    sLog.value,
    organization.value,
    "1.3.16", // version.value,
    List(file("readme.md"))
  )
)

lazy val root =
  project
    .in(file("."))
    .settings(commonSettings)
    .settings(name := "magnolia-root", publishArtifact := false)
    .aggregate(
      (core.projectRefs ++ examples.projectRefs ++ test.projectRefs): _*
    )

lazy val core = (projectMatrix in file("core"))
  .settings(commonSettings)
  .settings(
    name := "magnolia",
    version := "1.3.16",
    versionScheme := Some("early-semver")
  )
  .jvmPlatform(scalaVersions = List(scala3))
  .jsPlatform(scalaVersions = List(scala3))
  .nativePlatform(scalaVersions = List(scala3))

lazy val examples = (projectMatrix in file("examples"))
  .dependsOn(core)
  .settings(commonSettings)
  .settings(
    name := "magnolia-examples",
    publishArtifact := false
  )
  .jvmPlatform(scalaVersions = List(scala3))
  .jsPlatform(scalaVersions = List(scala3))
  .nativePlatform(scalaVersions = List(scala3))

lazy val test = (projectMatrix in file("test"))
  .dependsOn(examples)
  .settings(commonSettings)
  .settings(
    name := "magnolia-test",
    projectDependencies ++= Seq(
      "org.scalameta" %%% "munit" % "1.0.0-M12"
    ),
    testFrameworks += new TestFramework("munit.Framework"),
    publishArtifact := false
  )
  .jvmPlatform(scalaVersions = List(scala3))
  .jsPlatform(scalaVersions = List(scala3))
  .nativePlatform(scalaVersions = List(scala3))

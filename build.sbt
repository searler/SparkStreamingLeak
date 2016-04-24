name := "Spark Streaming Leak"

version := "0.2"

scalaVersion := "2.10.5"

libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.6.1" % "provided"

fork in run := true


mainClass in assembly := Some("TrivialCombineByKey")

assemblyJarName in assembly := "leak.jar"



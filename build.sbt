name := "Spark Streaming Leak"

version := "0.1"

scalaVersion := "2.10.4"

libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.3.1" % "provided"

fork in run := true

//javaOptions in run += "-verbose:class"

mainClass in assembly := Some("TrivialCombineByKey")

assemblyJarName in assembly := "leak.jar"



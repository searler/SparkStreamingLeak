name := "Spark Streaming Leak"

version := "0.1"

scalaVersion := "2.10.4"

libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.3.1"

fork in run := true

javaOptions in run += "-verbose:class"



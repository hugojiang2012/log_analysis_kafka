name := "log_analysis"

version := "1.0"

scalaVersion := "2.10.5"


libraryDependencies += "org.apache.spark" % "spark-streaming_2.10" % "1.6.3"
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.6.3"

//Spark SQL 依赖
libraryDependencies += "org.apache.spark" % "spark-sql_2.10" % "1.6.3"

// Spark Core依赖
libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.3"
libraryDependencies += "org.apache.spark" % "spark-hive_2.10" % "1.6.3"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"
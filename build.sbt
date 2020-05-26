name := "payment-events-spark"

version := "0.1"

scalaVersion := "2.12.1"
libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.12" % "2.4.5" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy"),
  "org.apache.spark" % "spark-sql_2.12" % "2.4.5" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy"),
  "org.apache.hadoop" % "hadoop-common" % "2.6.4" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy"),
  "org.apache.spark" % "spark-hive_2.12" % "2.4.5" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy"),
  "org.apache.spark" % "spark-yarn_2.12" % "2.4.5" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy"),
  "com.datastax.spark" %% "spark-cassandra-connector" % "2.5.0",
  "mysql" % "mysql-connector-java" % "8.0.20"
)
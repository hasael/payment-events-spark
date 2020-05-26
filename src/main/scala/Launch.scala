import com.datastax.spark.connector._
import models.TransactionData
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}
import reposiories.MySqlRepository

object Launch extends App {

  override def main(args: Array[String]): Unit = {

    val cassandraHost = "192.168.141.58"
    val cassandraPort = "30050"
    val cassandraKeySpace = "scalaevents"
    val cassandraTable = "events"

    val mySqlHost = "192.168.141.58"
    val mySqlUser = "root"
    val mySqlPass = "Password1!"
    val mySqlPort = "30060"
    val mySqlSchema = "ANALYTICS"
    val mySqlTable = "DAILY_AMOUNTS"

    val mySqlRepository = new MySqlRepository(mySqlHost, mySqlPort, mySqlUser, mySqlPass, mySqlSchema, mySqlTable)

    Logger.getLogger("org").setLevel(Level.ERROR)

    val conf = new SparkConf().
      setMaster("local").
      set("spark.driver.host", "127.0.0.1").
      set("spark.cassandra.connection.host", cassandraHost).
      set("spark.cassandra.connection.port", cassandraPort).
      setAppName("PaymentEventsSpark")

    val spark: SparkSession =
      SparkSession.builder.config(conf)
        .getOrCreate()
    val sc = spark.sparkContext
    sc.setLogLevel("ERROR")
    val rdd = sc.cassandraTable(cassandraKeySpace, cassandraTable)

    val columns = Seq("DATE", "AMOUNT", "CURRENCY")
    val schema = StructType(columns
      .map(fieldName => StructField(fieldName, StringType, nullable = true)))

    val data = rdd
      .groupBy(row => (row.getString("transaction_time").take(10), row.getString("currency")))
      .map(a => Row(
        a._1._1,
        a._2.foldLeft(0.0)((v: Double, row: CassandraRow) => row.getDouble("amount") + v).toString,
        a._1._2)
      )

    val ddf = spark.createDataFrame(data, schema)

    mySqlRepository.writeTable(ddf)

    println("Written: " + rdd.count)


  }

  private def toTransactionData(row: CassandraRow): TransactionData =
    TransactionData(row.getString("id"), row.getDouble("amount"), row.getString("currency"),
      row.getString("transaction_time"))
}


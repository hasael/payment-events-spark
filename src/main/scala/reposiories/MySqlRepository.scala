package reposiories

import org.apache.spark.sql.{DataFrame, SaveMode}

class MySqlRepository(host: String, port: String, user: String, pass: String, schema: String, table: String) {
  val prop = new java.util.Properties()
  prop.put("user", user)
  prop.put("password", pass)
  val url = "jdbc:mysql://" + host + ":" + port + "/" + schema

  def writeTable(ddf: DataFrame): Unit = {
    ddf.write.mode(SaveMode.Overwrite
    ).jdbc(url, table, prop)
  }
}

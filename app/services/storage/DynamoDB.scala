package services.storage

import javax.inject.Inject

import awscala._, dynamodbv2._

class DynamoDB @Inject() (configuration: play.api.Configuration) {

  // Read config
  val region = configuration.underlying.getString("aws.dynamoDB.region")
  val tableName = configuration.underlying.getString("aws.dynamoDB.table")

  // Initialize client and read table
  implicit val dynamoDB = DynamoDB.at(Region(region))
  val table: Table = dynamoDB.table(tableName).get


  def put(id: String, attributes: Seq[String, Any]): Unit = table.put(id, attributes)

  def put(id: String, attributes: Seq[String, Any]): Unit = table.put(id, attributes)
}

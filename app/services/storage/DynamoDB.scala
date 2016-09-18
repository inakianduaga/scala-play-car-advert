package services.storage

import javax.inject.{Inject, Singleton}

import awscala._
import dynamodbv2._

import scala.util.{Failure, Success, Try}

@Singleton
class DynamoDB @Inject() (configuration: play.api.Configuration) extends StorageDriverTrait {

  // Read config
  val region = configuration.underlying.getString("aws.dynamoDB.region")
  println(region)
  val tableName = configuration.underlying.getString("aws.dynamoDB.table")

  // Initialize client and read table
  implicit val dynamoDB = DynamoDB.at(Region(region))
  val table: Table = dynamoDB.table(tableName).get


  // CRUD //

  def index(): Try[Seq[StorableTrait]] = Try {
    table.scan(Seq("id" -> cond.isNotNull)).map(Storable(_))
  }

  def show(id: String): Try[StorableTrait] = {
    val item = table.get(id)
    if(item.isDefined) Success(Storable(item = item.get)) else Failure(new Throwable(s"advert w/ id $id not found"))
  }

  def update(data: StorableTrait): Try[Unit] = Try {
    table.put(data.id, data.attributes)
  }

  /**
    * If item doesn't exist, try to create it, otherwise fail
    */
  def create(data: StorableTrait): Try[StorableTrait] = {
    if(show(data.id).isFailure) {
      Try {
        table.put(data.id, data.attributes)
        data
      }
    } else {
      Failure(new Throwable("Item already exists"))
    }
  }

  def delete(id: String): Try[Unit] = Try {
    table.delete(id)
  }

  /**
    * Convert DynamoDB Item to StorableTrait-compatible class
    */
  case class Storable(_attributes: Seq[(String, Any)]) extends StorableTrait {
    val id: String = this._attributes.find(entry => entry._1 == "id").get._1
    val attributes = this._attributes.filter(entry => entry._1 != id)
  }

  object Storable {
    def apply(item: Item): StorableTrait =  Storable(item.attributes.map(a => (a.name, a.value)))
  }

}

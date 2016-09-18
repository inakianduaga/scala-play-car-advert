package services.storage

import javax.inject.{Inject, Singleton}

import awscala._
import dynamodbv2._

import scala.util.{Failure, Success, Try}

@Singleton
class DynamoDB @Inject() (configuration: play.api.Configuration) extends StorageDriverTrait {

  // Read config
  val tableName = configuration.getString("aws.dynamoDB.table").get

  // Initialize client and read table
  implicit val dynamoDB = dynamoClient
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
    table.put(data.id, data.attributes : _*)
  }

  /**
    * If item doesn't exist, try to create it, otherwise fail
    */
  def create(data: StorableTrait): Try[StorableTrait] = {
    if(show(data.id).isFailure) {
      Try {
        // http://stackoverflow.com/questions/10842851/scala-expand-list-of-tuples-into-variable-length-argument-list-of-tuples
        table.put(data.id, data.attributes : _*)
        data
      }
    } else {
      Failure(new Throwable("Item already exists"))
    }
  }

  def delete(id: String): Try[Unit] = Try {
    table.delete(id)
  }

  private def dynamoClient = {
    val isLocal: Boolean = configuration.getBoolean("aws.dynamoDB.local").get
    val localEndpoint = configuration.getString("aws.dynamoDB.localEndpoint").get

    if(isLocal) {
      val client = DynamoDB.local()
      client.setEndpoint(localEndpoint)
      client
    }
    else {
      // Read config
      val region = configuration.getString("aws.dynamoDB.region").get
      DynamoDB.at(Region(region))
    }
  }

  /**
    * Convert DynamoDB Item to StorableTrait-compatible class
    */
  case class Storable(_attributes: Seq[(String, Any)]) extends StorableTrait {
    val id: String = this._attributes.find(entry => entry._1 == "id").get._1
    val attributes = this._attributes.filter(entry => entry._1 != id)
  }

  object Storable {
    def apply(item: Item): StorableTrait =  Storable(item.attributes.map(a => (a.name, itemAttributeToPlain(a.value))))

    /**
      * Convert a DynamoDB item attribute to a "plain" value.
      * Note: This doesn't support nested structures
      */
    private def itemAttributeToPlain(value: AttributeValue): Any = {
      if(value.bl.isDefined) {
        value.bl.get
      } else if(value.s.isDefined) {
        value.s.get
      } else if (value.n.isDefined) {
        value.n.get
      } else {                          // NULL case
        ""
      }
    }
  }

}

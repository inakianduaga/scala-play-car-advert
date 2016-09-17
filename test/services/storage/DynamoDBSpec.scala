package services.storage

import javax.inject.Inject
import org.scalatestplus.play._
import awscala.dynamodbv2._
import com.amazonaws.services.{ dynamodbv2 => aws }
import org.slf4j._
import services.storage.{DynamoDB => Service}
import org.scalatest.BeforeAndAfter

/**
  * NOTE: A DynamoDB database must be runinng locally on port 8000 for these tests to work (see ./docker/dynamoDB/README.md)
  */
class DynamoDBSpec @Inject() (configuration: play.api.Configuration, service: Service) extends PlaySpec with BeforeAndAfter {

  case class Storable(id: String, attributes: Seq[(String, Any)]) extends StorableTrait {}

  // Set local endpoint
  implicit val dynamoDB = DynamoDB.local()
  val tableName = configuration.underlying.getString("aws.dynamoDB.table")

  /**
    * Recreate database before each test and seed it with some data
    * https://github.com/seratch/AWScala/blob/master/src/test/scala/awscala/DynamoDBV2Spec.scala#L18
    */
  before {
    // destroy table
    dynamoDB.table(tableName).get.destroy()

    // Create fresh table
    val createdTableMeta: TableMeta = dynamoDB.createTable(
      name = tableName,
      hashPK = "id" -> AttributeType.String
    )

    println(s"Waiting for DynamoDB table activation...")
    var isTableActivated = false
    while (!isTableActivated) {
      dynamoDB.describe(createdTableMeta.table).map { meta =>
        isTableActivated = meta.status == aws.model.TableStatus.ACTIVE
      }
      Thread.sleep(1000L)
      print(".")
    }
    println("")
    println(s"Created DynamoDB table has been activated.")

    // Populate database w/ test data
    println("seeding database")
    service.create(Storable("2", Seq("field" -> "value2")))
    service.create(Storable("1", Seq("field" -> "value")))
    service.create(Storable("3", Seq("field" -> "value3")))
    service.create(Storable("foobar", Seq("field" -> "value3")))

  }

  "DynamoDB storage service" must {

    "should return all items" in {
      assert(service.index().get.length == 4)
    }

    "should retrieve an item" in {
      assert(service.show("foobar").isSuccess)
    }

    "should throw when item is not found" in {
      assert(service.show("notAnExistingKey").isFailure)
    }

    "should create an item" in {
      assert(service.create(Storable("anotherField", Seq("field" -> "value3"))).isSuccess)
    }

    "should throw when attempting to create an item that already exists" in {
      assert(service.create(Storable("1", Seq("field" -> "value"))).isFailure)
    }

    "should delete an item" in {
      val countBeforeDeletion = service.index().get.length
      service.delete("1")
      val countAfterDeletion = service.index().get.length
      assert(countBeforeDeletion == countAfterDeletion -1)
    }
  }

}




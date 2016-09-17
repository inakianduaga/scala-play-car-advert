package services.storage

import org.scalatestplus.play._
import awscala.dynamodbv2._
import services.storage.{DynamoDB => Service}
import services.storage.StorableTrait
import org.scalatest.BeforeAndAfter

/**
  * NOTE: A DynamoDB database must be runinng locally on port 8000 for these tests to work (see ./docker/dynamoDB/README.md)
  */
class DynamoDBSpec extends PlaySpec with BeforeAndAfter {

  case class Storable(id: String, attributes: Seq[(String, Any)]) extends StorableTrait {


  }

  before {
    println("Running before this test")
  }
  after {
    println("Running after this test")
  }

  // Set local endpoint
  implicit val dynamoDB = DynamoDB.local()

  "DynamoDB storage service" must {


    "should return all items" in {
//      assert(AdvertNewCar(Some("12313"), "BMW M3", "Gasoline", 0).failed.get.getMessage == "Price must be non-zero")
    }

    "should retrieve an item" in {

    }

    "should throw when item is not found" in {

    }

    "should create an item" in {

    }

    "should throw when attempting to create an item that already exists" in {

    }

    "should delete an item" in {

    }
  }

}




package models

import java.text.SimpleDateFormat

import org.scalatestplus.play._
import play.api.libs.json.Json
import services.storage.SimpleStorable

class AdvertUsedCarSpec extends PlaySpec {

  "A Json Constructor" must {

    "fail when mileage is empty" in {
      val json = Json.parse(
        """ { "id" : "12313", "title": "BMW M3", "fuel": "Diesel", "price": 55, "mileage": 0, "first_registration": "2016-05-12" } """
      )
      assert(AdvertUsedCar(json).failed.get.getMessage == "Mileage must be non-zero")
    }

    "fail when date format is incorrect" in {
      val json = Json.parse(
        """ { "id" : "12313", "title": "BMW M3", "fuel": "Gasoline", "price": 55, "mileage": 1000, "first_registration": "notADate" } """
      )
      assert(AdvertUsedCar(json).isFailure)

    }
    "parse date correctly" in {
      val compareTo = new SimpleDateFormat("yyyy-MM-dd").parse("2016-05-12")
      val json = Json.parse(
        """ { "id" : "12313", "title": "BMW M3", "fuel": "Gasoline", "price": 55, "mileage": 1000, "first_registration": "2016-05-12" } """
      )
      assert(AdvertUsedCar(json).get.firstRegistration.equals(compareTo))
    }
  }

  "A Storable Constructor" must {

    "successfully hydrate when valid data is provided" in {
      val storable = SimpleStorable(
        "12313", Seq("title" -> "BMW M3", "fuel" -> "Gasoline", "price" -> 200, "mileage" -> 1000, "first_registration" -> "2016-05-12")
      )
      assert(AdvertUsedCar(storable).get.title == "BMW M3")
    }
    "fail when invalid data is provided" in {
      val storable = SimpleStorable("12313", Seq("title" -> "BMW M3", "fuel" -> "Gasoline", "price" -> 200, "mileage" -> 1000))
      assert(AdvertUsedCar(storable).isFailure)
    }
  }


  "A model" must {

    "serialize successfully to json" in {
      val json = Json.parse(
        """ { "id" : "abcde", "title": "BMW M3", "fuel": "Diesel", "price": 55, "mileage": 1000, "first_registration": "2016-05-12"} """
      )
      val expectedJson = Json.parse(
        """ { "id" : "abcde", "title": "BMW M3", "fuel": "Diesel", "price": 55, "new": false, "mileage": 1000, "first_registration": "2016-05-12" } """
      )
      val outputJson = AdvertUsedCar(json).get.toJson

      outputJson.mustEqual(expectedJson)
    }

  }

}
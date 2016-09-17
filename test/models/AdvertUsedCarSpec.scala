package models

import java.text.SimpleDateFormat
import org.scalatestplus.play._
import play.api.libs.json.Json

class AdvertUsedCarSpec extends PlaySpec {

  "A Constructor" must {

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
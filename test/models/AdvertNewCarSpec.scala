package models

import org.scalatestplus.play._
import play.api.libs.json.Json

class AdvertNewCarSpec extends PlaySpec {

  "A Constructor" must {
    "fail when price is 0" in {
      assert(AdvertNewCar(Json.parse(""" { "id" : "12313", "title": "BMW M3", "fuel": "Diesel", "price": 0 } """)).failed.get.getMessage ==
        "Price must be non-zero"
      )
    }

    "fail when fuel is not valid type" in {
      assert(AdvertNewCar(Json.parse(""" { "id" : "12313", "title": "BMW M3", "fuel": "Plutonium", "price": 200 } """)).failed.get.getMessage ==
        "Invalid fuel type"
      )
    }

    "fail when title is empty" in {
      assert(AdvertNewCar(Json.parse(""" { "id" : "12313", "title": "", "fuel": "Gasoline", "price": 200 } """)).failed.get.getMessage ==
        "Title must be non-empty"
      )
    }

    "auto-generate an id when empty" in {
      assert(AdvertNewCar(Json.parse(""" { "title": "BMW M3", "fuel": "Gasoline", "price": 55 } """)).get.id.length > 0)
    }
  }

  "A model" must {

    "serialize successfully to json" in {
      val json = Json.parse(
        """ { "id" : "abcde", "title": "BMW M3", "fuel": "Diesel", "price": 55} """
      )
      val expectedJson = Json.parse(
        """ { "id" : "abcde", "title": "BMW M3", "fuel": "Diesel", "price": 55, "new": true, "mileage": null, "first_registration": null } """
      )
      val outputJson = AdvertNewCar(json).get.toJson

      outputJson.mustEqual(expectedJson)
    }

  }

}

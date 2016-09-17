package models

import org.scalatestplus.play._

class AdvertNewCarSpec extends PlaySpec {

  "A Constructor" must {
    "should fail when price is 0" in {
      assert(AdvertNewCar(Some("12313"), "BMW M3", "Gasoline", 0).failed.get.getMessage == "Price must be non-zero")
    }

    "should fail when fuel is not valid type" in {
      assert(AdvertNewCar(Some("12313"), "BMW M3", "Plutonium", 55).failed.get.getMessage == "Invalid fuel type")
    }

    "should fail when title is empty" in {
      assert(AdvertNewCar(Some("12313"), "", "Gasoline", 55).failed.get.getMessage == "Title must be non-empty")
    }

    "must auto-generate id when empty" in {
      assert(AdvertNewCar(None, "BMW M3", "Gasoline", 55).get.id.length > 0)
    }
  }

}

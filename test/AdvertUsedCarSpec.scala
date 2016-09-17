import models.AdvertUsedCar
import org.scalatestplus.play._
import java.text.SimpleDateFormat

class AdvertUsedCarSpec extends PlaySpec {

  "A Constructor" must {
    "should fail when mileage empty" in {
      assert(AdvertUsedCar(Some("12313"), "BMW M3", "Gasoline", 55, 0, "2016-05-12").failed.get.getMessage == "Mileage must be non-zero")
    }
    "should fail when date format is incorrect" in {
      assert(AdvertUsedCar(Some("12313"), "BMW M3", "Gasoline", 55, 100, "notADate").isFailure)
    }
    "should parse date correctly" in {
      val compareTo = new SimpleDateFormat("yyyy-MM-dd").parse("2016-05-12")
      assert(AdvertUsedCar(Some("12313"), "BMW M3", "Gasoline", 55, 100, "2016-05-12").get.firstRegistration.equals(compareTo))
    }

  }

}

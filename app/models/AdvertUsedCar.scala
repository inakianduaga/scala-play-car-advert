package models
import java.text.SimpleDateFormat
import java.util.Date

import scala.util.{Failure, Success, Try}
import Fuel._
import services.validation.ValidationTrait
import services.storage.StorableTrait

case class AdvertUsedCar(
                         id: String,
                         title: String,
                         fuel: Fuel,
                         price: Int,
                         _new: Boolean,
                         mileage: Int,
                         firstRegistration: Date
                       ) extends StorableTrait {

    def toJson: String = {
      ""
    }

  }

object AdvertUsedCar extends ValidationTrait {

  /**
    * Special constructor to easily generate model:
    *  - Id will get generated automatically if not provided
    */
  def apply(_id: Option[String], title: String, _fuel: String, price: Int, mileage: Int, firstRegistration: String): Try[AdvertUsedCar] =
    validate(fuel= _fuel, price = price, date = firstRegistration, title = title, mileage = mileage).map(x => {
      val id = normalizeId(_id)
      val date = dateFromString(firstRegistration).get
      val fuel = Fuel.fromString(_fuel).get

      new AdvertUsedCar(id, title = title, fuel = fuel, price = price, _new = false, mileage = mileage, firstRegistration = date)
    })

  private def validate(fuel: String, price: Int, date: String, title: String, mileage: Int): Try[Unit] = {

    def validateDate(s: String): Try[Unit]      = dateFromString(s).map(_ => Unit)
    def validateMileage(amount: Int): Try[Unit] = if(amount > 0) Success(Unit) else Failure(new Throwable("Mileage must be non-zero"))

    // Return first exception or success
    List(
      validateFuel(fuel),
      validateDate(date),
      validateTitle(title),
      validatePrice(price),
      validateMileage(mileage)
    )
      .find(validation => validation.isFailure)
      .getOrElse(Success(Unit))
  }

  /**
    * http://stackoverflow.com/questions/16910344/how-to-convert-string-into-date-time-format-in-java
    */
  private def dateFromString(string: String): Try[Date] = Try(new SimpleDateFormat("yyyy-MM-dd").parse(string))

}
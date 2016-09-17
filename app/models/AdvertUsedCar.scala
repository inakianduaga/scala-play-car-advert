package models
import java.text.SimpleDateFormat
import java.util.Date
import scala.util.{Try, Success, Failure}
import java.text.ParseException
import Fuel._

case class AdvertUsedCar(
                         id: String,
                         title: String,
                         fuel: Fuel,
                         price: Int,
                         _new: Boolean,
                         mileage: Int,
                         firstRegistration: Date
                       ) extends AdvertCarTrait {

  }

object AdvertUsedCar {

  /**
    * Special constructor to easily generate model:
    *  - Id will get generated automatically if not provided
    */
  def apply(id: Option[String], title: String, fuel: String, price: Int, _new: Boolean, mileage: Int, firstRegistration: String) =
    validate(fuel= fuel, date = firstRegistration, title = title, mileage = mileage).map(x => {
      val id = normalizeId(id)
      val date = dateFromString(date).get
      val fuel = Fuel.fromString(fuel).get

      new AdvertUsedCar(id, title = title, fuel = fuel, price = price, _new = _new, mileage = mileage, firstRegistration = date)
    })

  private def validate(fuel: String, date: String, title: String, mileage: Int): Try[Unit] = {

    def validateFuel(s: String): Try[Unit] = if (Fuel.isFuelType(s)) Success(Unit) else Failure (new Throwable("Invalid fuel type"))

    def validateDate(s: String): Try[Unit] = dateFromString(s).map(_ => Unit)

    def validateTitle(s: String): Try[Unit] = if(s.length > 0) Success(Unit) else Failure(new Throwable("Title must be non-empty"))

    def validateMileage(amount: Int): Try[Unit] = if(amount > 0) Success(Unit) else Failure(new Throwable("Mileage must be non-zero"))

    // TODO: Merge all tries into a collection, maybe flatmapping the different tries?

    validateFuel(fuel) && validateDate(date) && validateTitle(title) && validateMileage(mileage)
  }

  private def dateFromString(string: String): Try[Date] = Try(SimpleDateFormat.parse(string))

  private def normalizeId(id: Option[String]): String = id.getOrElse(generateUuid())

  private def generateUuid(): String = java.util.UUID.randomUUID.toString



}
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
  def apply(id: Option[String], title: String, fuel: String, price: Int, _new: Boolean, mileage: Int, firstRegistration: String) = {
    if(validate(fuel= fuel, date = firstRegistration, title = title, mileage = mileage)) {
      val id = normalizeId(id)
      val date = dateFromString(date).get
      val fuel = Fuel.fromString(fuel).get

      new AdvertUsedCar(id, title = title, fuel = fuel, price = price, _new = _new, mileage = mileage, firstRegistration = date)
    }
  }

  private def validate(fuel: String, date: String, title: String, mileage: Int): Boolean = {

    def validateFuel(s: String): Boolean = Fuel.isFuelType(s)

    def validateDate(s: String): Boolean = dateFromString(s).isSuccess

    def validateTitle(s: String): Boolean = s.length > 0

    def validateMileage(amount: Int): Boolean = amount > 0

    validateFuel(fuel) && validateDate(date) && validateTitle(title) && validateMileage(mileage)
  }

  private def dateFromString(string: String): Try[Date] = {
    try {
      Success(SimpleDateFormat.parse(string))
    } catch {
      case e: ParseException => Failure(e)
    }
  }

  private def normalizeId(id: Option[String]): String = id.getOrElse(generateUuid())

  private def generateUuid(): String = java.util.UUID.randomUUID.toString



}
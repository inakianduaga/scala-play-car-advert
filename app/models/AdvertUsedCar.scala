package models
import java.text.SimpleDateFormat
import java.util.Date;
import scala.util.{Try, Success, Failure}
import java.text.ParseException
import Fuel._

case class AdvertUsedCar(
                         id: String,
                         title: String,
                         fuel: Fuel,
                         price: Int,
                         `new`: Boolean,
                         mileage: Int,
                         firstRegistration: Date
                       ) extends AdvertCarTrait {

  }

object AdvertUsedCar {

  def apply(id: Option[String], title: String, fuel: Fuel, price: Int, `new`: Boolean, mileage: Int, firstRegistration: String) = {
    if(validate()) {
      val date =
    }
  }

  private def validate(): Boolean = {
    true
  }

  private def dateFromString(string: String): Try[Date] = {
    try {
      Success(simpleDateFormat.parse(string))
    } catch {
      case e: ParseException => Failure(e)
    }
  }
}
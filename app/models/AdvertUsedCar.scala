package models

import java.text.SimpleDateFormat
import java.util.Date
import play.api.libs.json.JsValue
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

    def attributes: Seq[(String, Any)] = {
      Seq(
        "title" -> this.title,
        "fuel" -> this.fuel.toString,
        "price" -> this.price,
        "new" -> this._new,
        "mileage" -> this.mileage,
        "firstRegistration" -> this.firstRegistration.formatted("yyyy-MM-dd")
      )
    }
  }

object AdvertUsedCar extends ValidationTrait {

  /**
    * Generate model from Json
    * We read the Json manually, alternatively we could use
    * https://www.playframework.com/documentation/2.5.x/ScalaJson#JsValue-to-a-model
    */
  def apply(json: JsValue): Try[AdvertUsedCar] = Try {

    val _id = (json \ "id").toOption.map(_.toString)
    val title = (json \ "title").as[String]
    val _fuel = (json \ "fuel").as[String]
    val price = (json \ "price").as[Int]
    val mileage = (json \ "mileage").as[Int]
    val firstRegistration = (json \ "first_registration").as[String]

    validate(fuel= _fuel, title = title, price = price, date = firstRegistration, mileage = mileage).map(x => {
      val id = normalizeId(_id)
      val fuel = Fuel.fromString(_fuel).get
      val date = dateFromString(firstRegistration).get
      new AdvertUsedCar(id, title = title, fuel = fuel, price = price, _new = false, mileage = mileage, firstRegistration = date)
    })

  }.flatMap(x => x)


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
package models

import java.text.SimpleDateFormat
import java.util.Date
import play.api.libs.json._
import scala.util.{Failure, Success, Try}

import Fuel._
import services.json.JsonableTrait
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
                       ) extends StorableTrait with JsonableTrait {

    def attributes: Seq[(String, Any)] = {
      Seq(
        "title" -> this.title,
        "fuel" -> this.fuel.toString,
        "price" -> this.price,
        "new" -> this._new,
        "mileage" -> this.mileage,
        "first_registration" -> new SimpleDateFormat("yyyy-MM-dd").format(this.firstRegistration)
      )
    }

    def toJson: JsValue = JsObject(
      Seq(
        "id" -> JsString(this.id),
        "title" -> JsString(this.title),
        "fuel" -> JsString(this.fuel.toString),
        "price" -> JsNumber(this.price),
        "new" -> JsBoolean(this._new),
        "mileage" -> JsNumber(this.mileage),
        "first_registration" -> JsString(new SimpleDateFormat("yyyy-MM-dd").format(this.firstRegistration))
      )
    )

  }

object AdvertUsedCar extends ValidationTrait {

  /**
    * Hydrate model from Json
    * We read the Json manually, alternatively we could use
    * https://www.playframework.com/documentation/2.5.x/ScalaJson#JsValue-to-a-model
    */
  def apply(json: JsValue, idOverride: Option[String] = None): Try[AdvertUsedCar] = Try {

    val _id = if(idOverride.isDefined) idOverride else (json \ "id").toOption.map(_.as[String])
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

  /**
    * Hydrate model from StorableTrait
    */
  def apply(storable: StorableTrait): Try[AdvertUsedCar] = Try {

    def attributeByKey(key: String) = {
      storable.attributes.find(x => x._1 == key).get._2
    }

    AdvertUsedCar(JsObject(
      Seq(
        "id" -> JsString(storable.id),
        "title" -> JsString(attributeByKey("title").toString),
        "fuel" -> JsString(attributeByKey("fuel").toString),
        "price" -> JsNumber(attributeByKey("price").toString.toInt),
        "new" -> JsBoolean(false),
        "mileage" -> JsNumber(attributeByKey("mileage").toString.toInt),
        "first_registration" -> JsString(attributeByKey("first_registration").toString)
      )
    ))
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
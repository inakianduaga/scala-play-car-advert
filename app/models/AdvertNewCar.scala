package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.util.{Success, Try}

import Fuel._
import services.json.JsonableTrait
import services.validation.ValidationTrait
import services.storage.StorableTrait

case class AdvertNewCar(
                       id: String,
                       title: String,
                       fuel: Fuel,
                       price: Int,
                       _new: Boolean
                       )  extends StorableTrait with JsonableTrait {

  def attributes: Seq[(String, Any)] =
    Seq(
      "title" -> this.title,
      "fuel" -> this.fuel.toString,
      "price" -> this.price,
      "new" -> this._new
    )


  def toJson: JsValue = JsObject(
    Seq(
      "id" -> JsString(this.id),
      "title" -> JsString(this.title),
      "fuel" -> JsString(this.fuel.toString),
      "price" -> JsNumber(this.price),
      "new" -> JsBoolean(this._new),
      "mileage" -> JsNull,
      "first_registration" -> JsNull
    )
  )

}

object AdvertNewCar extends ValidationTrait {

  /**
    * Hydrate model from Json
    * We read the Json manually, alternatively we could use
    * https://www.playframework.com/documentation/2.5.x/ScalaJson#JsValue-to-a-model
    */
  def apply(json: JsValue): Try[AdvertNewCar] = Try {

    val _id = (json \ "id").toOption.map(_.as[String])
    val title = (json \ "title").as[String]
    val _fuel = (json \ "fuel").as[String]
    val price = (json \ "price").as[Int]

    validate(fuel= _fuel, title = title, price = price).map(x => {
      val id = normalizeId(_id)
      val fuel = Fuel.fromString(_fuel).get
      new AdvertNewCar(id, title = title, fuel = fuel, price = price, _new = true)
    })

  }.flatMap(x => x)

  /**
    * Hydrate model from StorableTrait
    */
  def apply(storable: StorableTrait): Try[AdvertNewCar] = Try {

    def attributeByKey(key: String) = {
      storable.attributes.find(x => x._1 == key).get._2
    }

    AdvertNewCar(JsObject(
      Seq(
        "id" -> JsString(storable.id),
        "title" -> JsString(attributeByKey("title").toString),
        "fuel" -> JsString(attributeByKey("fuel").toString),
        "price" -> JsNumber(attributeByKey("price").asInstanceOf[Int]),
        "new" -> JsBoolean(true),
        "mileage" -> JsNull,
        "first_registration" -> JsNull
      )
    ))
  }.flatMap(x => x)

  private def validate(fuel: String, title: String, price: Int): Try[Unit] =
    List(
      validateFuel(fuel),
      validateTitle(title),
      validatePrice(price)
    )
      .find(validation => validation.isFailure)
      .getOrElse(Success(Unit))

}

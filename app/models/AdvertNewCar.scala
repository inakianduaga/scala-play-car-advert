package models

import Fuel._
import play.api.libs.json._
import services.validation.ValidationTrait
import services.storage.StorableTrait

import scala.util.{Success, Try}

case class AdvertNewCar(
                       id: String,
                       title: String,
                       fuel: Fuel,
                       price: Int,
                       _new: Boolean
                       )  extends StorableTrait {

  def attributes: Seq[(String, Any)] = {
    Seq(
      "title" -> this.title,
      "fuel" -> this.fuel.toString,
      "price" -> this.price,
      "new" -> this._new
    )
  }
}

object AdvertNewCar extends ValidationTrait {

  /**
    * Generate model from Json
    * We read the Json manually, alternatively we could use
    * https://www.playframework.com/documentation/2.5.x/ScalaJson#JsValue-to-a-model
    */
  def apply(json: JsValue): Try[AdvertNewCar] = Try {

    val _id = (json \ "id").toOption.map(_.toString())
    val title = (json \ "title").get.toString
    val _fuel = (json \ "fuel").get.toString
    val price = (json \ "price").get.toString.toInt

    validate(fuel= _fuel, title = title, price = price).map(x => {
      val id = normalizeId(_id)
      val fuel = Fuel.fromString(_fuel).get
      new AdvertNewCar(id, title = title, fuel = fuel, price = price, _new = true)
    })

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

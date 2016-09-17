package models

import Fuel._
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
    * Special constructor to easily generate model:
    *  - Id will get generated automatically if not provided
    */
  def apply(_id: Option[String], title: String, _fuel: String, price: Int): Try[AdvertNewCar] =
    validate(fuel= _fuel, title = title, price = price).map(x => {
      val id = normalizeId(_id)
      val fuel = Fuel.fromString(_fuel).get
      new AdvertNewCar(id, title = title, fuel = fuel, price = price, _new = true)
    })

  private def validate(fuel: String, title: String, price: Int): Try[Unit] =
    List(
      validateFuel(fuel),
      validateTitle(title),
      validatePrice(price)
    )
      .find(validation => validation.isFailure)
      .getOrElse(Success(Unit))

}

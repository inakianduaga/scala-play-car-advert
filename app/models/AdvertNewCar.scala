package models
import Fuel._

case class AdvertNewCar(
                       id: String,
                       title: String,
                       fuel: Fuel,
                       price: Int,
                       `new`: Boolean,
                       ) extends AdvertCarTrait {

}

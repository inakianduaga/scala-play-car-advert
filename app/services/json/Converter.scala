package services.json

import play.api.libs.json.JsValue
import models._
import scala.util.Try

/**
  * Adapter to convert a Json value to a given model
  */
object Converter {

  def toAdvert(json: JsValue, id: Option[String] = None): Try[Either[AdvertNewCar, AdvertUsedCar]] = Try {
    val isNew = (json \ "new").toOption.isDefined
    if(isNew) Left(AdvertNewCar(json).get) else Right(AdvertUsedCar(json).get)
  }
}

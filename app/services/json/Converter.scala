package services.json

import play.api.libs.json.JsValue
import models._
import scala.util.Try

/**
  * Adapter to convert a Json value to a given model
  */
object Converter {

  def toAdvert(json: JsValue, idOverride: Option[String] = None): Try[Either[AdvertNewCar, AdvertUsedCar]] = Try {
    val isNew = (json \ "new").get.as[Boolean]
    if(isNew) Left(AdvertNewCar(json = json, idOverride = idOverride).get) else Right(AdvertUsedCar(json = json, idOverride = idOverride).get)
  }
}

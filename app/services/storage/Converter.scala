package services.storage

import models.{AdvertNewCar, AdvertUsedCar}
import scala.util.Try

object Converter {

  /**
    * Attempts to hydrate a storable to the corresponding model
    */
  def toAdvert(storable: StorableTrait): Try[Either[AdvertNewCar, AdvertUsedCar]] = Try {
    val isNew = storable.attributes.find(x => x._1 == "new").get._2.asInstanceOf[Boolean]
    if(isNew) Left(AdvertNewCar(storable).get) else Right(AdvertUsedCar(storable).get)
  }

}

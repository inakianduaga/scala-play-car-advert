package services.storage
import models.AdvertUsedCar
import models.AdvertNewCar
import scala.util.Try

trait StorageDriverTrait {

  /**
    * Returns all adverts
    */
  def index() : Try[List[Either[AdvertUsedCar, AdvertNewCar]]]

  def show(id: String): Try[Either[AdvertUsedCar, AdvertNewCar]]

  def create(advert: StorableTrait): Try[Either[AdvertUsedCar, AdvertNewCar]]

  def update(update: Either[AdvertUsedCar, AdvertNewCar]): Try[Either[AdvertUsedCar, AdvertNewCar]]

  def delete(advert: Either[AdvertUsedCar, AdvertNewCar]): Try[Unit]

}

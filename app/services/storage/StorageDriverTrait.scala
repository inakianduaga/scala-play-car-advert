package services.storage
import scala.util.Try

trait StorageDriverTrait {

  /**
    * Returns all adverts
    */
  def index() : Try[Seq[StorableTrait]]

  def show(id: String): Try[StorableTrait]

  def delete(id: String): Try[Unit]

  /**
    * Note: Create throws an exception if the advert already exists
    */
  def create(advert: StorableTrait): Try[StorableTrait]

  def update(update: StorableTrait): Try[Unit]

}

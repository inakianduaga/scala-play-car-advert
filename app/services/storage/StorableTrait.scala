package services.storage

/**
  * Interface for any item that can be stored
  */
trait StorableTrait {
  def attributes: Seq[(String, Any)]
  def id: String
}

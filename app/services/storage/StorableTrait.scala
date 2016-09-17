package services.storage

/**
  * Interface for any item that can be stored
  */
trait StorableTrait {
  def toJson: String
  def id: String
}

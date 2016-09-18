package services.storage

/**
  * Helper class to quickly initialize a Storable
  */
case class SimpleStorable(id: String, attributes: Seq[(String, Any)]) extends StorableTrait {}

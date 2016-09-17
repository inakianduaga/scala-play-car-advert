package models

import scala.util.{Failure, Success, Try}

trait AdvertCarTrait {

  def normalizeId(id: Option[String]): String = id.getOrElse(generateUuid())

  def validateFuel(s: String): Try[Unit]      = if (Fuel.isFuelType(s)) Success(Unit) else Failure (new Throwable("Invalid fuel type"))
  def validateTitle(s: String): Try[Unit]     = if(s.length > 0) Success(Unit) else Failure(new Throwable("Title must be non-empty"))
  def validatePrice(price: Int): Try[Unit]    = if (price > 0) Success(Unit) else Failure (new Throwable("Price must be non-zero"))

  private def generateUuid(): String = java.util.UUID.randomUUID.toString


}


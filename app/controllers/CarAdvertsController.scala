package controllers

import javax.inject._

import models._
import play.api._
import play.api.libs.json.{JsValue, Json, JsString}
import play.api.mvc.{Action, _}
import services.storage.StorageDriverTrait

import scala.util.Try
import services.json.{Converter => JsonConverter}
import services.storage.{Converter => StorableConverter}

/**
 * Cart adverts CRUD controller
 */
@Singleton
class CarAdvertsController @Inject() (storage: StorageDriverTrait) extends Controller {

  def index(sortBy: Option[String] = Some("id")): Action[AnyContent]  = Action {

    // Since it's quite hard handle all Try's until the very end, we wrap the call in a Try
    // and let the inner Trys rethrow when accessing them unsafely (.get)
    val operation: Try[Seq[JsValue]] = Try {
      storage
        .index()                                                  // Fetch all results as Seq[Storable]
        .get                                                      // Unwrap Try
        .map(StorableConverter.toAdvert)                          // Convert Storables to Adverts
        .map(_.get)                                               // Unwrap Try
        .sortBy(advert => advert.fold(_.id, _.id))                // Sort List: TODO: Add case class w/ sorting for each field
        .map(_.fold(_.toJson, _.toJson))                          // Convert Advert to Json
    }

    operation                                                     // Generate Response
      .map(Json.toJson(_))                                        // Convert List to Json
      .map(Ok(_))
      .getOrElse(
        BadRequest(JsString(operation.failed.get.getMessage))
      )
  }

  def show(id: String): Action[AnyContent] = Action {
    val operation = storage
      .show(id)
      .flatMap(StorableConverter.toAdvert)                        // Convert Storable to Advert
      .map(_.fold(_.toJson, _.toJson))                            // Convert Advert to Json

    operation
      .map(Ok(_))                                                 // Serve response
      .getOrElse(
        NotFound(JsString(operation.failed.get.getMessage))       // We'll assume exceptions are due to 404, so use 404 status
      )
  }

  def create: Action[JsValue] = Action(parse.json) { request =>
    val operation = JsonConverter
      .toAdvert(request.body)                                     // Convert json to advert
      .map(_.fold(storage.create(_), storage.create(_)))          // Perform database operation, get Storable
      .flatMap(x => x)                                            // Flatten Nested Try
      .flatMap(StorableConverter.toAdvert)                        // Convert Storable to Advert
      .map(_.fold(_.toJson, _.toJson))                            // Convert Advert to Json

    operation
      .map(x => Ok(x))
      .getOrElse(                                                 // Serve response
        BadRequest(JsString(operation.failed.get.getMessage))
      )
  }

  def update(id: String) = Action {
    Ok("")
  }

  def delete(id: String): Action[AnyContent] = Action {
    val operation = storage.delete(id)                            // Attemp to delete advert

    operation
      .map(x => Ok(JsString("")))                                 // empty ack response
      .getOrElse(                                                 // Serve response
        BadRequest(JsString(operation.failed.get.getMessage))
      )
  }

}

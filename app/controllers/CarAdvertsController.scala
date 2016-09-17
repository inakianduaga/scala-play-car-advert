package controllers

import javax.inject._
import play.api._
import play.api.libs.json.JsValue
import play.api.mvc._
import services.storage.StorageDriverTrait
import scala.util.Try

/**
 * Cart adverts CRUD controller
 */
@Singleton
class CarAdvertsController @Inject() (storage: StorageDriverTrait) extends Controller {

  def index = Action {
//    storage.index().

    Ok("")
  }

  def show(id: Int) = Action(parse.json) { request =>
    request.body
    Ok("")
  }

  def create(id: Int) = Action {
    Ok("")
  }

  def update(id: Int) = Action {
    Ok("")
  }

  def delete(id: Int) = Action {
    Ok("")
  }

  private def hydrateFromJson(json: JsValue) = {
//    Try {
//      val isNew = (json \ "new").get
//
////      if(isNew)
//    }

  }



}

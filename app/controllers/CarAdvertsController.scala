package controllers

import javax.inject._

import models._
import play.api._
import play.api.libs.json.JsValue
import play.api.mvc._
import services.storage.StorageDriverTrait

import scala.util.Try
import services.json.{Converter => JsonConverter}

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



    JsonConverter
      .toAdvert(request.body)                                 // Convert json to model
      .map(_ match {                                          // Perform database operation, get back StorageTrait
        case Left(x) => storage.show(x.id)
        case Right(x) => storage.show(x.id)
      })
      .flatMap(x => x.get.toSt)






    )


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

}

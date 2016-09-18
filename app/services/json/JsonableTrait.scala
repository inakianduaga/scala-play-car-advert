package services.json

import play.api.libs.json.JsValue

trait JsonableTrait {
  def toJson: JsValue
}

package core.database

import org.mongodb.scala.bson.collection.immutable.Document

import play.api.libs.json.JsValue
import play.api.libs.json.Json

object JsonValueImplicits {

  implicit def jsValueToString(jsValue: JsValue): String = {
    jsValue.toString()
  }
  
  implicit def documentToJsValue(document: Document): JsValue = {
    Json.parse(document.toJson())
  }

}
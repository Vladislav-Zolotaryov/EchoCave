package core.database

import org.mongodb.scala.bson.collection.immutable.Document
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.Writes
import org.joda.time.LocalDateTime
import play.api.libs.json.JsString
import org.joda.time.format.DateTimeFormat

object JsonValueImplicits {

  implicit def jsValueToString(jsValue: JsValue): String = {
    jsValue.toString()
  }
  
  implicit def documentToJsValue(document: Document): JsValue = {
    Json.parse(document.toJson())
  }
  
  implicit val jodaDateWrites: Writes[LocalDateTime] = new Writes[LocalDateTime] {
    def writes(date: LocalDateTime): JsValue = {
      val fmt = DateTimeFormat.forPattern("HH:mm:ss");
      JsString(date.toString(fmt))
    }
  }
  
  implicit def localDateTimeToJsValue(localDateTime:LocalDateTime): JsValue = {
    jodaDateWrites.writes(localDateTime)
  }

}
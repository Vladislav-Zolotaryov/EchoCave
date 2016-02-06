package models

import org.joda.time.LocalDateTime
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormat
import core.database.JsonValueImplicits._

case class Message(user:User, text:String, timestamp:LocalDateTime) {
  def this(user:User, text:String) = this(user, text, LocalDateTime.now)
}

object Message {
  
  implicit val writer: Writes[Message] = new Writes[Message] {
    def writes(message: Message): JsValue = {
      JsObject(
         Seq(
          ("user", JsString(message.user.username)),
          ("message", JsString(message.text)),
          ("timestamp", localDateTimeToJsValue(message.timestamp)) 
         )
      )
    }
  }
  
  implicit def messageToJsValue(message:Message):JsValue = {
    writer.writes(message)
  }
  
  def apply(user:User, text:String) = new Message(user, text)
}
package actors

import akka.actor.Actor
import akka.actor.Props
import akka.event.Logging
import models.User
import models.ChatRoom
import models.Message
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent._
import play.api.libs.iteratee.{ Concurrent, Enumerator, Iteratee }
import play.api.libs.json.JsValue
import play.Logger

class ChatRoomActor extends Actor {

  import ChatRoomActor._

  val chatRoom = new ChatRoom()
  val (enumerator, channel) = Concurrent.broadcast[JsValue]

  def receive = {
    case JoinRoom(user) =>
      chatRoom.join(user)
      self ! Broadcast(
        Message(
          User.SYSTEM,
          "User " + user.username + " has joined"
        )
      )

      val iteratee = Iteratee.foreach[JsValue] { message =>
        self ! WriteMessage(user, message.\("text").get.toString())
      }
      sender ! (iteratee, enumerator)

    case WriteMessage(user, text) =>
      val msg = Message(user, text)
      chatRoom.write(msg)
      self ! Broadcast(msg)

    case Broadcast(msg: JsValue) =>
      Logger.debug("Broadcasting " + msg)
      channel.push(msg)

    case _ =>
      Logger.error("Received unknown message")
  }

}

object ChatRoomActor {

  def props = Props[ChatRoomActor]

  case class JoinRoom(user: User)
  case class WriteMessage(user: User, text: String)
  case class Leave(user: User)
  case class Broadcast(msg: JsValue)

}
package controllers

import core.database.Secure
import play.api.mvc.Controller
import play.api.mvc.Action
import com.google.inject.Inject
import akka.actor.ActorSystem
import actors.ChatRoomActor
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.duration._
import akka.pattern.ask
import akka.util.Timeout
import models.Message
import actors.ChatRoomActor.JoinRoom
import models.User
import actors.ChatRoomActor.WriteMessage
import play.api.mvc.WebSocket
import play.api.libs.iteratee.Iteratee
import play.api.libs.iteratee.Enumerator
import scala.concurrent.Await
import java.util.concurrent.TimeUnit
import java.util.logging.Logging
import play.api.Logger
import play.api.libs.json.JsValue
import play.api.libs.json.JsString

class Chat @Inject() (system: ActorSystem) extends Controller with Secure {

  implicit val timeout: Timeout = Timeout(15.seconds)

  val chatRoomActor = system.actorOf(ChatRoomActor.props, "chat-room-actor")

  def joinRoom = WebSocket.using[JsValue] { request =>
    request.session.get("username") match {
      case Some(username) =>
        val future = User.find(username).flatMap { user =>
            val channelsFuture = chatRoomActor ? JoinRoom(user)
            channelsFuture.mapTo[(Iteratee[JsValue, _], Enumerator[JsValue])]
        }
        Await.result(future, Duration(30, TimeUnit.SECONDS))
      case None => 
        (Iteratee.ignore[JsValue], Enumerator(JsString("Incorrect user!")))
    }
  }
  
  def chat = withAuth { user => request =>
    Ok(views.html.chat(request))
  }
  
}
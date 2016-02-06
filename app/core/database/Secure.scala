package core.database

import models.User
import play.api.mvc._
import scala.concurrent._
import ExecutionContext.Implicits.global
import controllers.Auth
import controllers.routes

trait Secure {

  def username(request: RequestHeader) = request.session.get(Security.username)
  
  def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Auth.login())
  
  def withAuth(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(username, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }
  }
  
  def withUser(f: User => Request[AnyContent] => Result) = withAuth { username => implicit request =>
    User.find(username).value.map { user =>
      f(user.get)(request)
    }.getOrElse(onUnauthorized(request))
  }
  
}
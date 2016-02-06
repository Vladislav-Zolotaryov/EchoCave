package controllers

import java.util.concurrent.TimeUnit

import scala.concurrent._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

import core.database.Secure
import models.User
import play.api._
import play.api.data._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

class Auth extends Controller with Secure {

  val loginForm = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(User.apply)(User.unapply) verifying ("Invalid username or password", result => result match {
      case (user) => Await.result((User.authenticate(user.username, user.password)), Duration(30, TimeUnit.SECONDS))
    })
  )
  
  val registerForm = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(User.apply)(User.unapply)
  )
  
  def login = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => { BadRequest(views.html.login(formWithErrors)) },
      user => { Redirect(routes.Chat.chat()).withSession("username" -> user.username) }
    )
  }
  
  def loginPage = Action {
    Ok(views.html.login(loginForm))
  }
  
  def register = Action.async { implicit request =>
    registerForm.bindFromRequest.fold(
      formWithErrors => Future { BadRequest(views.html.login(formWithErrors)) },
      user => {
        User.create(user).map {
          x => Redirect(routes.Chat.chat()).withSession("username" -> user.username)
        }
      }
    )
  }
  
  def logout = Action {
    Redirect(routes.Auth.login).withNewSession.flashing(
      "success" -> "You are now logged out."
    )
  }

}
package core.database

import play.Play

object Config {
  
  val configuration = Play.application().configuration()
  
  def get(key: String): String = {
    configuration.getString(key)
  }
  
}
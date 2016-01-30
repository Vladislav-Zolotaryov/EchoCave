package models

import scala.concurrent.Future

import org.mongodb.scala.Completed
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.collection.immutable.Document

import core.database.DatabaseComponent
import play.api.libs.json.Json
import play.api.libs.json.Writes

case class User(username: String, password: String)

object User extends DatabaseComponent {
  
  final val COLLECTION_NAME = "User"
    
  def create(item: User): Future[Completed] = { 
    collection().insertOne(item).head()
  }

  def find(username: String): Future[Seq[User]] = {
    collection().find[User](Document("username" -> username)).toFuture()
  }
 
  def collection():MongoCollection[User] = {
    db.getCollection(COLLECTION_NAME)
  }
  
}
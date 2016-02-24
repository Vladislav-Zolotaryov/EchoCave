package models

import scala.concurrent.Future
import org.mongodb.scala.Completed
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.collection.immutable.Document
import scala.concurrent._
import ExecutionContext.Implicits.global
import core.database.DatabaseComponent
import play.api.libs.json.Json
import play.api.libs.json.Writes
import play.api.libs.json.Reads
import scala.util.Success
import scala.util.Failure

case class User(username: String, password: String)

object User extends DatabaseComponent {
  
  final val COLLECTION_NAME = "User"
  
  final val SYSTEM:User = User("System", "System")
  
  implicit val jsonReader = Json.reads[User]
  implicit val jsonWriter = Json.writes[User]
    
  implicit def documentToUser(document: Document): User = {
    jsonReader.reads(Json.parse(document.toJson())).get
  }
  
  implicit def userToDocument(user: User): Document = {
    Document(jsonWriter.writes(user).toString())
  }
  
  def create(item: User): Future[Completed] = { 
    collection().insertOne(item).head()
  }

  def find(username: String): Future[User] = {
    collection().find(Document("username" -> username)).head().map { document => 
      document
    }
  }
  
  def authenticate(username: String, password: String): Future[Boolean] = {
    val result = collection().find(Document("username" -> username, "password" -> password)).head()
    result map {
      _ => true
    } recover {
      case _ => false
    }
  }  
  
  def collection():MongoCollection[Document] = {
    db.getCollection(COLLECTION_NAME)
  }
  
}
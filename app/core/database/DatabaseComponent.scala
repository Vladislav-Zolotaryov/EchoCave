package core.database

import scala.concurrent._
import play.api.Logger
import ExecutionContext.Implicits.global
import org.mongodb.scala.Completed

trait DatabaseComponent {
  
  def db = MongoDb.getDatabase();
  
  def dropAllCollections(): Future[Seq[Seq[Completed]]] = {
    Logger.debug("Dropping all collections")
    val namesFutures = db.listCollectionNames().toFuture()
    namesFutures.flatMap { names => 
      
      val collectionFutures:Seq[Future[Seq[Completed]]] = names map { name =>
        Logger.debug("Creating a future to drop collection " + name)
        db.getCollection(name).drop().toFuture()
      }
      
      Future.sequence(collectionFutures)
    }
  }
  
}
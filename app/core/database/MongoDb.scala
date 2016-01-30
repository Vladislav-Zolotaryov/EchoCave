package core.database

import org.mongodb.scala.MongoClient
import org.mongodb.scala.MongoDatabase

object MongoDb {
  
  val mongoClient = MongoClient("mongodb://" + Config.get("mongodb.url"))
    
  def getDatabase(): MongoDatabase = mongoClient.getDatabase(Config.get("mongodb.name"))
  
}
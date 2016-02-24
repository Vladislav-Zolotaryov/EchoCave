package models

import scala.collection.mutable.ArrayBuffer

class ChatRoom(val messageTreshhold: Int = 50) {
  
  val users = new ArrayBuffer[User]
  val messages = new ArrayBuffer[Message]
  
  def join(user:User) {
    users += user
  }
  
  def leave(user:User) {
    users -= user
  }
  
  def write(message:Message) {
    if (users contains message.user) {
      addMessage(message)
    } else {
      throw new RuntimeException("User is not from this room")
    }
  }
  
  def addMessage(message:Message) {
    messages += message
    if (messages.size > messageTreshhold) {
      messages.remove(0);
    }
  }
    
  def getMessages(): List[Message] = {
    return messages.toList
  }
  
  def getUsers(): List[User] = {
    return users.toList
  }
  
}
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import models.ChatRoom
import models.User
import models.Message

@RunWith(classOf[JUnitRunner])
class ChatRoomSpec extends Specification {

  "Chat Room" should {
    
    "Allow user to join" in {
      val chatRoom = new ChatRoom()
      val testUser = User("Test", "TEST");
      chatRoom.join(testUser)   
      chatRoom.users should contain(testUser)
    }
    
    "Allow user to write" in {
      val chatRoom = new ChatRoom()
      val testUser = User("TestUserName", "TEST")
      val testMessage = Message(testUser, "TEST_MSG")
      chatRoom.join(testUser)
      chatRoom.write(testMessage)
      chatRoom.getMessages() should contain(testMessage)
    }
    
    "Char Room should discard messages greater then N" in {
      val chatRoom = new ChatRoom(3)
      val testUser = User("TestUserName", "TEST")
      val testMessage = Message(testUser, "TEST_MSG")
      chatRoom.join(testUser)
      for( a <- 1 to 10) {
        chatRoom.write(testMessage)
      }
      chatRoom.getMessages().size should beEqualTo(3)
    }
    
  }
  
}
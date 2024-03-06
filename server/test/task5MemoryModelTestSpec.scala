import org.scalatestplus.play.PlaySpec
import models._


class task5MemoryModelTestSpec extends PlaySpec {
    "Task5MemoryModel" must {
        "do valid login for default user web" in {
            Task5MemoryModel.validateInfo("web", "apps") mustBe (true)
        }
       "do valid login for default user mlewis" in {
            Task5MemoryModel.validateInfo("mlewis", "prof") mustBe (true)
    }
    "reject login for existing user with wrong password" in {
            Task5MemoryModel.validateInfo("mark", "apps") mustBe(false)
    }
    "reject login for nonexisting user with another user's password" in {
            Task5MemoryModel.validateInfo("notReal" , "prof") mustBe(false)
    }
    "reject account Creation with existing user" in {
        Task5MemoryModel.createUser("mlewis","prof") mustBe(false)
    }
    "accept account creation with new user" in {
        Task5MemoryModel.createUser("noah", "wenig") mustBe(true)
    }
    "accept login with new user" in {
        Task5MemoryModel.validateInfo("noah", "wenig") mustBe(true)
    }
    "getting global messages should display defualt ones" in {
        Task5MemoryModel.getGlobalMessages()mustBe(
        List[(String, String)] (("web", "This is a global message"), 
                                ("mlewis", "hello everyone! Scala is cool!"),
                                ("mlewis", "Python sucks")))
    }
    "send a global message" in {
        Task5MemoryModel.sendGlobalMessage("noah", "testGlobalMsg")
        Task5MemoryModel.getGlobalMessages()mustBe(
            List[(String, String)] (("web", "This is a global message"), 
                                ("mlewis", "hello everyone! Scala is cool!"),
                                ("mlewis", "Python sucks"),
                                ("noah", "testGlobalMsg")))
        
    }
    "default user web should see default local message" in {
        Task5MemoryModel.getLocalMessages("web") mustBe(
     List(("mlewis", "Hello Web! I love scala")))
    }
    "default user mlewis should not see web's default local message" in {
        Task5MemoryModel.getLocalMessages("mlewis") mustBe(
            List()
        )
    }
    "mlewis should be able to send a message to newly created user" in {
        Task5MemoryModel.sendLocalMessage("mlewis", "noah", "hello noah")
        Task5MemoryModel.getLocalMessages("noah") mustBe(
            List(("mlewis", "hello noah"))
        )
    }
}
}

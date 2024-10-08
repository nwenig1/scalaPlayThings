package models
import collection.mutable
import java.util.LinkedList

object Task5MemoryModel {
  private val users = mutable.Map[String, String] ("web" -> "apps", "mlewis" -> "prof")
  private val localMessages = mutable.Map[String, List[(String, String)]]("web" -> List(("mlewis", "Hello Web! I love scala")))

  private var globalMessages =  List[(String, String)] (("web", "This is a global message"), 
                                                        ("mlewis", "hello everyone! Scala is cool!"),
                                                        ("mlewis", "Python sucks"))
    def validateInfo(username:String, password:String) : Boolean = {
        users.get(username).map(_== password).getOrElse(false)
    }
    def createUser(username: String, password:String) : Boolean = {
        if(users.contains(username)){
            false 
        }else{
            users(username) = password
            true 
        }
    }
 
      def getGlobalMessages() : Seq[(String, String)] = {
        globalMessages
    }
    def getLocalMessages(username : String) : Seq[(String, String)] = {
      localMessages.get(username).getOrElse(Nil)
    }
    def sendGlobalMessage(username: String, gMessage : String): Unit = {
      globalMessages= globalMessages.appended((username, gMessage))
    }
    def sendLocalMessage(username : String, recipientName : String, lMessage : String):Unit = { //FOR TASK 5
      recipientName.trim()
      if(doesUserExist(username) && doesUserExist(recipientName)){ //if valid names on both ends, add to model (flashing in controller)
      localMessages(recipientName) = (username, lMessage) :: localMessages.get(recipientName).getOrElse(List())
      } 
  }
  //variant that returns a boolean for task 8, since I get the messages thru ajax, not returning w/ model
  def sendLocalMessageTask8(username: String, recipientName : String, lMessage: String): Boolean = {
    if(doesUserExist(username) && doesUserExist(recipientName)){
      localMessages(recipientName) = (username, lMessage) :: localMessages.get(recipientName).getOrElse(List())
      true 
    }else{
      false 
    }
  }
    //helper method for sending local messages 
    def doesUserExist(username: String) : Boolean ={
      return users.contains(username)
    }
}

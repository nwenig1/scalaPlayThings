package models
import collection.mutable
import java.util.LinkedList

object Task5MemoryModel {
  private val users = mutable.Map[String, String] ("web" -> "apps", "mlewis" -> "prof")
 // private val localMessages = mutable.Map[String, List[String]] ()
//  private val globalMessages = mutable.Map[String, List[String]] ("mlewis" ->List("This is a global message!", "I love Scala"),"web"-> List("Hello everyone!")
//USE TUPLES SEXY SCALA 
  private val globalMessages =  List[String] ("This is a global message", "hello everyone!")


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
  //  def getGlobalMessages(): mutable.Map[String,Seq[String]] = {
      def getGlobalMessages() : Seq[String] = {
        globalMessages
    }
   // def getLocalMessages(username:String) : mutable.Map[String, List[String]] = {

   // }
}

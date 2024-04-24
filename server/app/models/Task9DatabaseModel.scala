package models

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext
import models.Tables._
import scala.concurrent.Future
import org.mindrot.jbcrypt.BCrypt

class Task9DatabaseModel(db: Database)(implicit ec: ExecutionContext) {
  
  def validateUser(username: String, password: String) : Future[Option[Int]] = {
    val matches = db.run(Task9user.filter(userRow => userRow.username === username).result)
    matches.map(userRows => userRows.headOption.flatMap{
      userRow => if(BCrypt.checkpw(password, userRow.password)) Some(userRow.userid) else None
    })
  }
  def createUser(username: String, password: String) : Future[Option[Int]] = {
    println("in model create user username is " + username + " and password is " + password)
    val matches = db.run(Task9user.filter(userRow => userRow.username === username).result)
    matches.flatMap{ userRows =>
      if(userRows.isEmpty){
        db.run(Task9user += Task9userRow(-1, username, BCrypt.hashpw(password, BCrypt.gensalt())))
        .flatMap { addCount =>
          println("add count is: " + addCount)
          if(addCount > 0){
            println("user added, add count bigger than 0")
             db.run(Users.filter(userRow => userRow.username === username).result).map(_.headOption.map(_.id))
          }
      else {
        println("in else, about to return none")
        Future.successful(None)
      }
    }
        
      } else Future.successful(None)
    }
  }
  def sendGlobalMessage (sender: String, content: String) = {
    db.run(Globalmessages += GlobalmessagesRow(-1, sender, content))
  }
  //might need to verify the reciever exists 
  def sendLocalMessage(sender: String, reciever: String, content: String): Future[Boolean] = {
    val userExists = db.run(Task9user.filter(userRow => userRow.username === reciever).result)
    userExists.flatMap{ userRows =>
      if(userRows.isEmpty){
    Future.successful(false)
      } else {
         db.run(Localmessages += LocalmessagesRow(-1, sender, reciever, content))
        Future.successful(true)
          }
      }
    }
  def getLocalMessages(username:String ) : Future[Seq[LocalMessage]] = {
   val messages = db.run(Localmessages.filter(lm => lm.lmsgreceiver === username).result)
        messages.map(localMessages => localMessages.map(localMessage => LocalMessage(localMessage.lmsgsender, localMessage.lmsgreceiver, localMessage.lmsgcontent)))

}
def getGlobalMessages() : Future[Seq[GlobalMessage]] = {
  val messages = db.run(Globalmessages.filter(gm => gm.gmsgsender === gm.gmsgsender).result) //make this a map, not a filter?
  messages.map(globalMessages => globalMessages.map(globalMessage =>GlobalMessage(globalMessage.gmsgsender, globalMessage.gmsgcontent)))
}
    
           

  }


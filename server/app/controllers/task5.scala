package controllers

import javax.inject._
import shared.SharedMessages
import play.api.mvc._
import models.Task5MemoryModel


@Singleton
class task5 @Inject() (cc:ControllerComponents) extends AbstractController(cc) {
  
    def login = Action { implicit request =>
        Ok(views.html.login5())
    }
    def validateInfo = Action { implicit request =>
        val postInfo = request.body.asFormUrlEncoded
        postInfo.map { args =>
            val username = args("username").head
            val password = args("password").head 
            if(Task5MemoryModel.validateInfo(username, password)){
                Redirect(routes.task5.showMessages).withSession("username" -> username).flashing("success" -> s"Logged in as $username")
            } else {
                Redirect(routes.task5.login).flashing("error" -> "Invalid username/password combination")
            }
        }.getOrElse(Redirect(routes.task5.login))
      
    }
    def createUser = Action { implicit request =>
        val postVals = request.body.asFormUrlEncoded
        postVals.map { args =>
            val username = args("username").head
            val password = args("password").head
            if(Task5MemoryModel.createUser(username, password)){
                Redirect(routes.task5.showMessages).withSession("username" -> username).flashing("success" -> s"Account created for user $username!")
            }else{
                Redirect(routes.task5.login).flashing("error" -> "Issue creating account. Try again or use the login form if you have one already")
            }
         }.getOrElse(Redirect(routes.task5.login))
    }
    def showMessages = Action { implicit request =>
        val usernameOption = request.session.get("username")
        usernameOption.map { username=>
          //  val localMessages = Task5MemoryModel.getLocalMessages(username)
            val globalMessages = Task5MemoryModel.getGlobalMessages()
            Ok(views.html.messages(globalMessages))  //add localMessages once working 
        }.getOrElse(Redirect(routes.task5.login))
    }
}

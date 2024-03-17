package controllers

import javax.inject._
import shared.SharedMessages
import play.api.mvc._
@Singleton
class task6 @Inject() (cc:ControllerComponents) extends AbstractController(cc) {
  
    def canvas() = Action { implicit request=>
        Ok(views.html.task6())
    }
    def socket = ???
        //WebSocket.accept ???
        //dunno what types to use 
}

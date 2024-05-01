package controllers




import javax.inject._
import shared.SharedMessages
import play.api.mvc._
import models.Task5MemoryModel //using same memory model 
import play.api.libs.json._
import models._
import play.api.libs.streams.ActorFlow

@Singleton
class Task7 @Inject() (cc:ControllerComponents) extends AbstractController(cc) {

    def load = Action{ implicit request =>
        Ok(views.html.task7())
    }
    def socket = WebSocket.accept[String, String] { request =>
        println("getting socket")
      
        }
}
//manager has a  map of actor ref to locations
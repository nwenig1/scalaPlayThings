package controllers



import javax.inject._
import shared.SharedMessages
import play.api.mvc._
import models.Task5MemoryModel //using same memory model 
import play.api.libs.json._
import models._
import actors.PaletteActor
import actors.PaletteManager
import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.libs.streams.ActorFlow
import akka.actor.Props




@Singleton
class Chess @Inject() (cc:ControllerComponents)(implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {
    def load() = Action{ implicit request =>
        Ok(views.html.chess())
    }
    def socket() = Action { implicit request =>
        ???
    }

}

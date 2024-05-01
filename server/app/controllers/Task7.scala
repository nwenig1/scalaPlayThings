package controllers




import javax.inject._
import shared.SharedMessages
import play.api.mvc._
import models.Task5MemoryModel //using same memory model 
import play.api.libs.json._
import models._
import play.api.libs.streams.ActorFlow
import akka.actor.Props
import actors._
import akka.actor.ActorSystem
import akka.stream.Materializer


@Singleton
class Task7 @Inject() (cc:ControllerComponents)(implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {
    val manager = system.actorOf(Props[SpriteManager](), "Manager")
    def load = Action{ implicit request =>
        Ok(views.html.task7())
    }
    def socket = WebSocket.accept[String, String] { request =>
      println("getting socket")
      ActorFlow.actorRef { out =>
        SpriteActor.props(out, manager)
        }
      
        }
}
//manager has a  map of actor ref to locations
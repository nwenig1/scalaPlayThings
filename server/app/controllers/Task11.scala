package controllers



import javax.inject._
import shared.SharedMessages
import play.api.mvc._
import models.Task5MemoryModel //using same memory model 
import play.api.libs.json._
import models._

@Singleton
class Task11 @Inject() (cc:ControllerComponents) extends AbstractController(cc) {
    def load= Action{ implicit request=>
        Ok(views.html.task11())
    }
}

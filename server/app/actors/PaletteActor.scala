package actors

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import play.api.libs.json.Json
import actors.PaletteManager
import actors.PaletteManager._
import shared.Drawable
import shared.Rectangle
import shared.Line
import play.api.libs.json.Reads
import shared.Circle




class PaletteActor(out: ActorRef, manager: ActorRef) extends Actor{
    manager ! PaletteManager.NewPalette(self)
    import PaletteActor._

  
    implicit val RectangleReads: play.api.libs.json.Reads[shared.Rectangle] = Json.reads[Rectangle]
    implicit val LineReads: play.api.libs.json.Reads[shared.Line]= Json.reads[Line]
    implicit val CircleReads: play.api.libs.json.Reads[shared.Circle]= Json.reads[Circle]

    implicit val RectangleWrites: play.api.libs.json.OWrites[shared.Rectangle]= Json.writes[Rectangle]
    implicit val LineWrites: play.api.libs.json.OWrites[shared.Line]= Json.writes[Line]
    implicit val CircleWrites: play.api.libs.json.OWrites[shared.Circle] = Json.writes[Circle]
    implicit val DrawableWrites: play.api.libs.json.OWrites[shared.Drawable] = Json.writes[Drawable]  
    def receive = {
       case s: String=> {
        //attempts to parse to each shape type. if one fails (is None), it attempts the next shape
        val optRect: Option[Rectangle] = Json.parse(s).asOpt[Rectangle]
            optRect match {
                case Some(rectangle) => {
                    manager ! NewDrawing(rectangle)
                }
                case None => {
                    val optLine =Json.parse(s).asOpt[Line]
                    optLine match {
                        case Some(line) => manager ! NewDrawing(line)
                        case None=> {
                            val optCircle = Json.parse(s).asOpt[Circle]
                            optCircle match{
                                case Some(circle) => manager ! NewDrawing(circle)
                                case None => {
                                    println("Unknown thing received")
                                }
                            }
                        }
                    }
                }
            }
        }
       case SendDrawings(shapes) =>  out ! Json.toJson(shapes).toString()
    }
}

object PaletteActor{
    def props(out: ActorRef, manager: ActorRef) = Props(new PaletteActor(out, manager))
    case class SendDrawings(drawings: List[Drawable])
}
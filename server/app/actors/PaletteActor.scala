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

  
    implicit val RectangleReads = Json.reads[Rectangle]
    implicit val LineReads = Json.reads[Line]
    implicit val CircleReads = Json.reads[Circle]

    implicit val RectangleWrites = Json.writes[Rectangle]
    implicit val LineWrites = Json.writes[Line]
    implicit val CircleWrites = Json.writes[Circle]

  
    def receive = {
       case s: String=> {
        println("server received string: " + s)
        val optRect: Option[Rectangle] = Json.parse(s).asOpt[Rectangle]
            optRect match {
              
                case Some(rectangle) => {
                    println(rectangle)
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
                                    println("socket connected or unknown thing received")
                                }
                            }
                        }
                    }
                }
            }
        }
       case SendDrawings(shapes) => {
       // println("sending all shapes out: " + shapes)
        var strShapes = List.empty[String]
        //convert to seq strings, send that seq to client
       shapes.map { shape =>
           strShapes ::= shape.toString()
           }
       println("Shapes as strings : " + strShapes)
        val readyToSendShapes = Json.toJson(strShapes).toString()
        out ! readyToSendShapes
       


        
        //out ! shapes
       }

    }

}

object PaletteActor{
    def props(out: ActorRef, manager: ActorRef) = Props(new PaletteActor(out, manager))
    case class SendDrawings(drawings: List[Drawable])
   

}
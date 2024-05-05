package actors

import akka.actor.Actor
import akka.actor.ActorRef
import collection.mutable
import shared.Drawable
import actors.PaletteActor.SendDrawings

class PaletteManager extends Actor {
    private var palettes = List.empty[ActorRef] //list of all ppl connected
    private var shapes = List.empty[Drawable] //list of all shapes 
    import actors.PaletteManager._
  
    def receive = {
        case NewPalette(palette) => palettes ::= palette
        case NewDrawing(drawing) => {
            println("Manager recieved " + drawing)
            shapes ::= drawing //adds shape to list of all drawings
           //  println("all shapes" + shapes)
            for(palette <- palettes){ //sends the drawings out to everyone 
                palette ! SendDrawings(shapes)
            }

        }

    }
}
object PaletteManager{
    case class NewPalette(palette: ActorRef)
    case class NewDrawing(drawing: Drawable)
}

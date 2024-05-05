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
        case NewPalette(palette) => {
            palettes ::= palette
            palette ! SendDrawings(shapes)
        }
        case NewDrawing(drawing) => {
            shapes ::= drawing //adds shape to list of all drawings
            //sends the drawings out to everyone 
            for(palette <- palettes){ 
                palette ! SendDrawings(shapes)
            }
        }
    }
}
object PaletteManager{
    case class NewPalette(palette: ActorRef)
    case class NewDrawing(drawing: Drawable)
}

package actors

import akka.actor.Actor
import akka.actor.ActorRef
import collection.mutable
import actors.SpriteActor.newCoord


class SpriteManager extends Actor{
    private var sprites = List.empty[ActorRef]

    private var spritePos = mutable.Map[ActorRef, String]()
    import actors.SpriteManager._

    def receive = {
        //i have no idea if that will work 
        case NewSprite(sprite) => sprites ::= sprite
        case Action(sprite, coord) => {
            spritePos(sprite) =  coord
            var allLocations = List[String]()
            for(sprite <- sprites){
               allLocations ::=spritePos(sprite)
            }
            for(sprite <- sprites){
                sprite ! newCoord(allLocations)
            }
        }

        case m => println("unhandled receive input in chat manager")
    }
  
}
object SpriteManager{
    case class NewSprite(sprite: ActorRef)
    case class Action(sprite: ActorRef, action: String)
    
}

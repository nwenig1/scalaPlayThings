package actors

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import play.api.libs.json.Json


class SpriteActor(out: ActorRef, manager: ActorRef) extends Actor{
    manager ! SpriteManager.NewSprite(self)
    
    import SpriteActor._

    def receive = {
      case coord: String => {
        var coord2 = "["+coord+"]"
        manager ! SpriteManager.Action(self, coord2)
      }
      case newCoord(locations) => {
        val jsonLocations = Json.toJson(locations).toString()
        out ! jsonLocations
        
          
      }
      case e => println("unhandled case in sprite actor receaDkfhW;EOIFLve")
    }
  
}

object SpriteActor{
    def props(out: ActorRef, manager: ActorRef) = Props(new SpriteActor(out, manager))
    case class newCoord(locations: List[String])
    //should move have the actor ref being moved? 
    //otherwise wouldnt be able to know which sprite to move
    //msg in chat is only msg cuz u dont need to know who sent what 
    //but this needs to know who moves
    //so has the sprite being moved, then the direction being moved 
}
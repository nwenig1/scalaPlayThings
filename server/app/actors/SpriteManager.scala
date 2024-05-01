package actors

import akka.actor.Actor
import akka.actor.ActorRef
import collection.mutable


class SpriteManager extends Actor{
    private val sprites = mutable.Map[ActorRef, (Int, Int)]()
    import actors.SpriteManager._

    def receive = {
        case NewSprite(sprite) => sprites(sprite) = (0, 0) //new sprites start at 0,0
        case Action("up") => 
    }
  
}
object SpriteManager{
    case class NewSprite(sprite: ActorRef)
    case class Action(action: String)
    
}

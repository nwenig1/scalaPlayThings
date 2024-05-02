package actors

import akka.actor.Actor
import akka.actor.ActorRef
import collection.mutable


class SpriteManager extends Actor{
    private val sprites = mutable.Map[ActorRef, (Int, Int)]()
    import actors.SpriteManager._

    def receive = {
        //i have no idea if that will work 
        case NewSprite(sprite) => sprites(sprite) = (0, 0) //new sprites start at 0,0
        case Action(sprite, "left") => sprites(sprite) = (sprites(sprite)._1-1, sprites(sprite)._2)
        case Action(sprite, "right") => sprites(sprite) = (sprites(sprite)._1+1, sprites(sprite)._2)
        case Action(sprite, "down") => sprites(sprite) = (sprites(sprite)._1, sprites(sprite)._2+1) //increase?? cuz graphics stuff
        case Action(sprite, "up") => sprites(sprite) = (sprites(sprite)._1, sprites(sprite)._2-1) //decreae??
        case m => println("unhandled receive input in chat manager")
    }
  
}
object SpriteManager{
    case class NewSprite(sprite: ActorRef)
    case class Action(sprite: ActorRef, action: String)
    
}

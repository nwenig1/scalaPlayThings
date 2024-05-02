package actors

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef


class SpriteActor(out: ActorRef, manager: ActorRef) extends Actor{
    manager ! SpriteManager.NewSprite(self)
    
    import SpriteActor._

    def receive = {
      ??? 
    }
    out ! "Connected"
}

object SpriteActor{
    def props(out: ActorRef, manager: ActorRef) = Props(new SpriteActor(out, manager))

    case class move(sprite: ActorRef, action: String)
    //should move have the actor ref being moved? 
    //otherwise wouldnt be able to know which sprite to move
    //msg in chat is only msg cuz u dont need to know who sent what 
    //but this needs to know who moves
    //so has the sprite being moved, then the direction being moved 
}
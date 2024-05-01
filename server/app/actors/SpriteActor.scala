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

    case class move(action: String)
}
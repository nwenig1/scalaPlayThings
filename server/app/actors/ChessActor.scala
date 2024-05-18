package actors

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import play.api.libs.json.Json
import play.api.libs.json.Reads

import shared._
import play.api.libs.json.JsValue
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsError

class ChessActor(out: ActorRef, manager: ActorRef) extends Actor{
    manager ! ChessManager.NewPlayer(self)
    import ChessActor._
implicit val RookDataReads = Json.reads[RookData]
    def receive = {
       
        case s: String => {
            val optRook = Json.parse(s).asOpt[RookData]
            optRook match { 
                case Some(rook) => println("got rook data" + rook)
                case None => println("issue with thing")
            }
        }

    }
}

object ChessActor{
    def props(out: ActorRef, manager: ActorRef) = Props(new ChessActor(out, manager))
    case class SendBoard(board: List[Piece])
}

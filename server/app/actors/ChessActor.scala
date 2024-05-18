package actors

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import play.api.libs.json.Json
import play.api.libs.json.Reads

import shared._

class ChessActor(out: ActorRef, manager: ActorRef) extends Actor{
    manager ! ChessManager.NewPlayer(self)

//implicit val RookReads = Json.reads[Rook] 
//implicit val BishopReads = Json.reads[Bishop] 
//implicit val KnightReads = Json.reads[Knight] 
//implicit val KingReads = Json.reads[King]
//implicit val QueenReads = Json.reads[Queen]
//implicit val PawnReads = Json.reads[Pawn]
//implicit val PieceReads = Json.reads[Piece]

    def receive = {
        case s: String => {
           
        }

    }
}

object ChessActor{
    def props(out: ActorRef, manager: ActorRef) = Props(new ChessActor(out, manager))
    case class SendBoard(board: List[Piece])
}

package actors

import akka.actor.Actor
import akka.actor.ActorRef
import collection.mutable
import shared._
import actors.ChessActor.SendBoard

class ChessManager extends Actor{

    private var allPieces = List.empty[Piece]
    
    private var allConnected = List.empty[ActorRef]
    import actors.ChessManager._

    def receive = {
        case NewPlayer(player) => {
            allConnected ::= player
           // player ! SendBoard(allPieces)
        }
        case Move(sprite, board) => {

            allPieces = board
           }
           for(connected <- allConnected){
         connected ! SendBoard(board)
         
        }
    }
}

object ChessManager{
    case class NewPlayer(player: ActorRef)
    case class Move(sprite: ActorRef, newBoard: List[Piece])
    
}

package actors

import akka.actor.Actor
import akka.actor.ActorRef
import collection.mutable
import shared._

import actors.ChessActor.ReceiveBoard
import scala.concurrent.java8.FuturesConvertersImpl.P

class ChessManager extends Actor{
    private val startingBoard: List[PieceData] = List(
        new RookData("white", "A", "1"), 
        new KnightData("white", "B", "1"),
        new BishopData("white", "C", "1"),
        new QueenData("white", "D", "1"), 
        new KingData("white", "E", "1"), 
        new BishopData("white","F", "1"), 
        new KnightData("white", "G", "1"),  
        new RookData("white", "H", "1"), 
        new PawnData("white","A", "2"),
        new PawnData("white","B", "2"),
        new PawnData("white","C", "2"),
        new PawnData("white","D", "2"),
        new PawnData("white","E", "2"),
        new PawnData("white","F", "2"),
        new PawnData("white","G", "2"),
        new PawnData("white","H", "2"),

        new RookData("black", "A", "8"), 
        new KnightData("black", "B", "8"),
        new BishopData("black", "C", "8"), 
        new QueenData("black", "D", "8"), 
        new KingData("black", "E", "8"), 
        new BishopData("black","F", "8"), 
        new KnightData("black","G", "8"), 
        new RookData("black", "H", "8"), 
        new PawnData("black","A", "7"),
        new PawnData("black","B", "7"),
        new PawnData("black","C", "7"),
        new PawnData("black","D", "7"),
        new PawnData("black","E", "7"),
        new PawnData("black","F", "7"),
        new PawnData("black","G", "7"),
        new PawnData("black","H", "7")
      )
    
    private var currentGameState:GameState = new GameState("white", startingBoard)
    
    private var whitePlayer:Option[ActorRef] = None
    private var blackPlayer:Option[ActorRef] = None
    private var allConnected = List.empty[ActorRef]
    import actors.ChessManager._

    def receive = {
        case NewPlayer(player) => {
            println("got new player connected")
            //makes white player the first person connected, black person second connected
            //all others only go to allconnected
            whitePlayer match {
                case None=> whitePlayer = Some(player)
                case Some(_) => blackPlayer match{
                    case None => blackPlayer = Some(player)
                    case Some(_) => 
                }
            }
            allConnected ::= player
           player ! SendBoard(currentGameState)
        }
        //turn changes on client, to server gets the next move. if white made a move, should be black's turn
        case ReceiveBoard(gameState, sender) => {
           gameState.currentTurn match{
            case "black" => {
                if(Some(sender).equals(whitePlayer)){
                currentGameState = gameState
                  for(connected <- allConnected){
         connected ! SendBoard(currentGameState)
         
        }
            } else println("invalid player tried to make move on white turn")
        }
            case "white" =>{        
                if(Some(sender).equals(blackPlayer)){
                    currentGameState = gameState
                      for(connected <- allConnected){
         connected ! SendBoard(currentGameState)
         
        }
            } else println("invalid player tried to make move on black turn")
            }
            case "reset" => {
                whitePlayer = None
                blackPlayer = None
                currentGameState = new GameState("white", startingBoard)
                                      for(connected <- allConnected){
         connected ! SendBoard(currentGameState)
                                      }
            }
        }
        
                
        }
    }
}

object ChessManager{
    case class NewPlayer(player: ActorRef)
    case class SendBoard(gameState: GameState)
    
    
}

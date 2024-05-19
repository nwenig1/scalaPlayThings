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
import play.api.libs.json.OWrites
import actors.ChessManager.SendBoard

class ChessActor(out: ActorRef, manager: ActorRef) extends Actor{
    manager ! ChessManager.NewPlayer(self)
    import ChessActor._
implicit val RookDataWrites: play.api.libs.json.OWrites[shared.RookData] = Json.writes[RookData]
implicit val RookDataReads: play.api.libs.json.Reads[shared.RookData] =Json.reads[RookData]
implicit val KnightDataWrites: play.api.libs.json.OWrites[shared.KnightData] = Json.writes[KnightData]
implicit val KnightDataReads: play.api.libs.json.Reads[shared.KnightData]= Json.reads[KnightData]
implicit val BishopDataWrites: play.api.libs.json.OWrites[shared.BishopData] = Json.writes[BishopData]
implicit val BishopDataReads: play.api.libs.json.Reads[shared.BishopData]= Json.reads[BishopData]
implicit val KingDataWrites: play.api.libs.json.OWrites[shared.KingData]= Json.writes[KingData]
implicit val KingDataReads: play.api.libs.json.Reads[shared.KingData]= Json.reads[KingData]
implicit val QueenDataWrites: OWrites[QueenData]= Json.writes[QueenData]
implicit val QueenDataReads: play.api.libs.json.Reads[shared.QueenData] = Json.reads[QueenData]
implicit val PawnDataWrites: play.api.libs.json.OWrites[shared.PawnData]= Json.writes[PawnData]
implicit val PawnDataWReads: play.api.libs.json.Reads[shared.PawnData]= Json.reads[PawnData]

implicit val PieceDataReads: play.api.libs.json.Reads[shared.PieceData] = Json.reads[PieceData]
implicit val PieceDataWrites: play.api.libs.json.OWrites[shared.PieceData]= Json.writes[PieceData]

implicit val GameStateWrites: play.api.libs.json.OWrites[shared.GameState]= Json.writes[GameState]
implicit val GameStateReads: play.api.libs.json.Reads[shared.GameState]= Json.reads[GameState]


    def receive = {
       
        case s: String => {
            println("socket got a String:")
           
            val optGameState = Json.parse(s).asOpt[GameState]
            optGameState match {
                case Some(gameState) => manager ! ReceiveBoard(gameState, self)
                case None => println("got none in opt parse")
            }
           
        }
        case board: SendBoard => {
            out ! Json.toJson(board.gameState).toString()
        }

    }
}

object ChessActor{
    def props(out: ActorRef, manager: ActorRef) = Props(new ChessActor(out, manager))
    case class ReceiveBoard(gameState: GameState, player: ActorRef)
}

package shared


/*for sending data through json
was struggling to serialize/deserialize the full fleged piece classes
documentation was wonky, but seemed like Json.reads/writes only works with case classes 
these contain all the needed data to regenerate the piece in its position
server will hold the piece data, client will run off of the full fledged pieces
to send, all pieces are converted to piece data which gets sent
client will receive the list of piece data and regenerate the board using the data 
*/
sealed trait PieceData

case class GameState(currentTurn: String, allPieceData: List[PieceData])
case class RookData(color: String, rX: String, rY: String) extends PieceData
case class KnightData(color: String, kX:String, kY: String) extends PieceData
case class BishopData(color: String, bX: String, bY: String) extends PieceData
case class KingData(color:String, kX:String, kY:String) extends PieceData
case class QueenData(color:String, qX: String, qY:String) extends PieceData
case class PawnData(color:String, pX:String, pY:String) extends PieceData

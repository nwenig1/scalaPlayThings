package edu.trinity.videoquizreact
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.document
import scala.scalajs.js.annotation.JSExportTopLevel
import slinky.web.html.canvas
import shared._
import play.api.libs.json.Json
import play.api.libs.json.OWrites
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsError


object Chess {
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


    
    var selectedPiece: Option[Piece] = None
    var board = List.empty[Piece] //set on socket open
    var turn = "white"  //set on socket open
    def runChess() : Unit = {
        println("trying to run chess")
        val socketRoute = document.getElementById("ws-route").asInstanceOf[html.Input].value
        var socket = new dom.WebSocket(socketRoute.replace("http", "ws"))
        socket.onopen = (event) => socket.send(Json.toJson("New user connected").toString())
        
        val canvas = document.getElementById("chessCanvas").asInstanceOf[html.Canvas]
        val ctx = canvas.getContext("2d")
        canvas.style.border = "1px solid black"
        socket.onmessage = (event) => {
            Json.fromJson[GameState](Json.parse(event.data.toString())) match {
                case JsSuccess(gameState, _) => {
                    turn = gameState.currentTurn
                    board = dataToPiece(gameState.allPieceData)
                    ctx.clearRect(0, 0, canvas.width, canvas.height)
                    drawPieces(canvas, board)
                    document.getElementById("currentTurn").asInstanceOf[html.Span].innerHTML = turn
                }
                case JsError(error) => println("Issue w/ game state parse on client side ")
            }
        }
        //this function selects pieces and does the move 
        canvas.addEventListener("mousedown", { (e0: dom.Event) =>
            val e = e0.asInstanceOf[dom.MouseEvent] 
            val xy = getCursorPosition(canvas, e)
            val clickedSquare: Position =  new Position((findLetter(xy._1, canvas.width), findNum(xy._2, canvas.height)))
            selectedPiece match{
            //if a piece is selected and square is clicked, try to move piece to that square 
                case Some(piece) => { 
                    selectedPiece = None
                if(piece.side.equals(turn)){
                val newBoard = makeMove(piece, clickedSquare, false)
                newBoard match {
                    case Some(newBoard) => {
                        switchTurns()
                        if(isCheckmated(board, turn)){
                            document.getElementById("currentTurn").asInstanceOf[html.Span].innerHTML = "Game over!!"
                            println("checkmated!! game is over")
                        }else {
                        //send game data
                            val allPieceData = getAllPieceData(newBoard)
                            val currentGameState = new GameState(turn, allPieceData)
                            socket.send(Json.toJson(currentGameState).toString())   
                        }
                    }
                    case None => println("got none back")
                }
                } else println("Attempting to move piece who's turn it isnt ")
            }
            //if no piece is selected, find the piece on clicked square, and select it
            //map is easier than find, dont wanna deal with opt stuff 
                case None => board.map(piece => if(piece.curPosition.equals(clickedSquare)) selectedPiece = Some(piece))
            }
        })
        document.getElementById("resetGame").asInstanceOf[html.Button].addEventListener("click", { (e0: dom.Event) =>
            socket.send(Json.toJson(new GameState("reset", List.empty[PieceData])).toString)
        
        })
    }

    def makeMove(piece: Piece, destination: Position, hypothetical: Boolean): (Option[List[Piece]]) = {
        val previousSquare = piece.curPosition
        var ret: List[Piece] = List.empty[Piece]
            if(piece.validMoves(board).contains(destination)){
                //gets new board if capturing a piece (piece is on the dest square)
                ret = board.filterNot(piece => piece.curPosition.equals(destination)) 
                //moving piece to new destination
                piece.curPosition = destination
                /*
                 for pieces where I care if they've moved yet
                rooks/kings for castling, pawns for the double move 
                this function is used for checks/checkmate making a "hypothetical move"
                pieces first moves were being ran on hypothetical moves, so pawns couldnt move twice
                if called in a checmate/check 'check', won't affect hasMoved state
                if called in a player's move, will affect hasMoved state
                */
                if(!hypothetical){
                    piece match {
                    //this might cause weird behavior if trying to move one of these pieces for the first time
                    //while not in check, and the move wouldn't get you out of check
                    //will run anyways, even tho position would be reset 
                        case pawn: Pawn => pawn.hasMoved = true 
                        case rook: Rook => rook.hasMoved = true 
                        case king: King => king.hasMoved = true
                        case _ => //do nothing 
                    }
                }
                if(notInCheck(ret, turn)) return Some(ret)
                else {
                    println("can't move there, you'd be in check")
                    piece.curPosition = previousSquare
                    return None
                }
            } 
            else{
                println("was not in valid moves")
                return None
            }
    }
    def notInCheck(hypoBoard: List[Piece], currentPlayer: String): Boolean = {
        var kingPos = new Position('Z', 100)
        hypoBoard.map(piece =>{
            piece match{
                case king: King => if(king.side.equals(currentPlayer)) kingPos = king.curPosition
                case _ => 
                }
            })
        val opposingPieces = hypoBoard.filter(piece => !piece.side.equals(currentPlayer))
        var allOpposingMoves = List.empty[Position]
        opposingPieces.map(opposingPiece => allOpposingMoves = allOpposingMoves ++ opposingPiece.validMoves(hypoBoard))
        if(allOpposingMoves.contains(kingPos)) false 
        else true 
        }
    def isCheckmated(allPieces: List[Piece], currentPlayer: String): Boolean = {
        val playersPieces = allPieces.filter(piece => piece.side.equals(currentPlayer))
        var checkmated = true 
        playersPieces.map(piece =>{ 
            val initialPosition = piece.curPosition
            piece.validMoves(allPieces).map(candidateMove => {
            val hypoBoard = makeMove(piece, candidateMove, true)
            hypoBoard match {
                case Some(possibleMove) => {
                    checkmated = false 
                }
                case None =>
                }
            })
            piece.curPosition = initialPosition
        })
        return checkmated
    }
  
    def drawPieces(canvas: html.Canvas, pieces: List[Piece]) = {
        val ctx = canvas.getContext("2d")
        //ideally this can be used to remake the board every time a move is made
        //can also be used for initial board state, and gets passed in all the pieces and their initial positions
         val w=canvas.width
        val h = canvas.height
        val rSize = h * 1/8
        val odds = Seq[Int](0, 2, 4, 6)
        val evens = Seq[Int](1, 3, 5, 7)
        for(oddRow <- odds){

            odds.map(num => ctx.fillRect(w*num/8, h*oddRow/8, rSize, rSize))
        }
        for(evenRow <- evens){
            evens.map(num => ctx.fillRect(w*num/8, h*evenRow/8, rSize, rSize))
        }
        pieces.map(piece => {
            val img= dom.document.createElement("img").asInstanceOf[dom.html.Image]
            piece match{
                case rook: Rook => {  
                    if(rook.side == "white") img.src="assets/images/wRook.jpg"
                        else  img.src = "assets/images/bRook.jpg"
                }
                case knight: Knight => {
                    if(knight.side == "white") img.src="assets/images/wKnight.jpg"
                        else img.src = "assets/images/bKnight.jpg"
                }
                case bishop: Bishop => {
                    if(bishop.side =="white") img.src="assets/images/wBishop.jpg"
                    else img.src = "assets/images/bBishop.jpg"
                }
                case king: King => {
                    if(king.side == "white") img.src="assets/images/wKing.jpg"
                    else img.src = "assets/images/bKing.jpg"
                }
                case queen: Queen => {
                    if(queen.side == "white") img.src = "assets/images/wQueen.jpg"
                    else img.src = "assets/images/bQueen.jpg"
                }
                case pawn: Pawn => {
                    if(pawn.side == "white") img.src = "assets/images/wPawn.jpg"
                    else img.src = "assets/images/bPawn.jpg"
                }
                case _ => println("unhrecognized piece found")
            }
            val coords = findCoords(piece.curPosition, canvas.height)
              img.onload = (_:dom.Event) => ctx.drawImage(img, coords._1, coords._2, 45, 45)
        })


    }

    def getCursorPosition(canvas : html.Canvas, event: dom.MouseEvent): (Int, Int) = {
        val rect = canvas.getBoundingClientRect()
        val x =(event.clientX - rect. left).toInt
        val y = (event.clientY - rect.top).toInt
        (x, y)
    }

    def findLetter(x:Int, width: Int): Character = {
        if(x<width* 1/8) 'A'
        else if(x>width*1/8 && x<width*2/8) 'B'
        else if(x>width*2/8 && x<width*3/8) 'C'
        else if(x>width*3/8 && x<width*4/8) 'D'
        else if(x>width*4/8 && x<width*5/8) 'E'
        else if(x>width*5/8 && x<width*6/8) 'F'
        else if(x>width*6/8 && x<width*7/8) 'G'
        else if(x>width*7/8 && x<width) 'H'
        else 'Z' //just for error handling, should never get to this case realistically
    }
    def findNum(y:Int, height:Int): Int = {
        if(y<height* 1/8) 8
        else if(y>height*1/8 && y<height*2/8) 7
        else if(y>height*2/8 && y<height*3/8) 6
        else if(y>height*3/8 && y<height*4/8) 5
        else if(y>height*4/8 && y<height*5/8) 4
        else if(y>height*5/8 && y<height*6/8) 3
        else if(y>height*6/8 && y<height*7/8) 2
        else if(y>height*7/8 && y<height) 1
        else 100 //just for error handling, should never get to this case realistically
        
    }
    //helper function to figure out what coordinates a square is. 
    //does the oppisite of findNum/letter, goes the other way
    //helpful for putting pieces in certain squares
    def findCoords(pos: Position, height: Int): (Int, Int) = {
        val xLetter = pos.square._1
        val yNumber = pos.square._2
        var xCoord = 999
        var yCoord = 999
        xLetter.toString.charAt(0) match{
            case 'A' => xCoord = 15
            case 'B' => xCoord = (height * 1/8) + 15
            case 'C' => xCoord = (height *2/8) + 15
            case 'D' => xCoord = (height * 3/8) + 15
            case 'E' => xCoord = (height * 4/8) + 15
            case 'F' => xCoord = (height * 5/8) + 15
            case 'G' => xCoord = (height * 6/8) + 15
            case 'H' => xCoord = (height * 7/8) + 15
            case _  => println("invalid position in letter match ")
            
        }
        yNumber match {
            case 8=> yCoord = 7
            case 7 => yCoord = (height * 1/8) + 10
            case 6 => yCoord = (height *2/8) + 10
            case 5 => yCoord = (height * 3/8) + 10
            case 4 => yCoord = (height * 4/8) + 10
            case 3 => yCoord = (height * 5/8) + 10
            case 2 => yCoord = (height * 6/8) + 10
            case 1 => yCoord = (height * 7/8) + 10
            case _  => println("invalid position in number match")
        }
        (xCoord, yCoord)
    }

    def switchTurns() : Unit = {
        if(turn.equals("white")){
            turn = "black"
        } else turn = "white"
        println("turn is " + turn)
    }
    //used to convert all pieces into json sendable format. 
    //takes board variable, and converts all pieces into their Data counterpart
    //see PieceData trait in shared for more info 
    def getAllPieceData(newBoard : List[Piece]): List[PieceData] = {
        var ret = List.empty[PieceData]
        newBoard.map(piece => {
            piece match {
            case rook:Rook => ret::= new RookData(rook.side, rook.curPosition.square._1.toString, rook.curPosition.square._2.toString)
            case knight: Knight => ret::= new KnightData(knight.side, knight.curPosition.square._1.toString, knight.curPosition.square._2.toString)
            case bishop:Bishop => ret ::= new BishopData(bishop.side, bishop.curPosition.square._1.toString, bishop.curPosition.square._2.toString)
            case king:King => ret::= new KingData(king.side, king.curPosition.square._1.toString, king.curPosition.square._2.toString)
            case queen:Queen => ret::= new QueenData(queen.side, queen.curPosition.square._1.toString, queen.curPosition.square._2.toString)
            case pawn: Pawn=> ret::= new PawnData(pawn.side, pawn.curPosition.square._1.toString, pawn.curPosition.square._2.toString)
            case _ => println("unkown type in getAllPieceData")
                }
            })
        ret
    }
    //takes list of PieceDatas (after being received from server) and converts to real pieces
    //used in deserializing json input 
    def dataToPiece(allPieceData: List[PieceData]): List[Piece] = {
        var ret = List.empty[Piece]
        allPieceData.map(pieceData => {
            pieceData match {
            case rook: RookData => ret::= new Rook(rook.color, new Position(rook.rX.charAt(0), rook.rY.toInt))
            case knight: KnightData => ret::= new Knight(knight.color, new Position(knight.kX.charAt(0), knight.kY.toInt))
            case bishop:BishopData => ret::= new Bishop(bishop.color, new Position(bishop.bX.charAt(0), bishop.bY.toInt))
            case king:KingData => ret::= new King(king.color, new Position(king.kX.charAt(0), king.kY.toInt))
            case queen:QueenData => ret ::= new Queen(queen.color, new Position(queen.qX.charAt(0), queen.qY.toInt))
            case pawn: PawnData => ret::= new Pawn(pawn.color, new Position(pawn.pX.charAt(0), pawn.pY.toInt))
            case _ => println("unknown piece data found in dataToPiece")
            }
        })
        ret 
    }
}

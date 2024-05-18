package edu.trinity.videoquizreact
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.document
import scala.scalajs.js.annotation.JSExportTopLevel
import slinky.web.html.canvas
import shared.Piece
import shared.Position
import shared.Rook
import shared.King
import shared.Bishop
import shared.Queen
import shared.Knight
import shared.Pawn
import slinky.web.html.blockquote
import play.api.libs.json.Json
import shared.RookData


object Chess {
implicit val RookDataWrites = Json.writes[RookData]
    var board = List.empty[Piece]
    var selectedPiece: Option[Piece] = None
    var turn = "white"
    def runChess() : Unit = {
        println("trying to run chess")
        val socketRoute = document.getElementById("ws-route").asInstanceOf[html.Input].value
        var socket = new dom.WebSocket(socketRoute.replace("http", "ws"))
        socket.onopen = (event) => {
            val testRook = new Rook("white", new Position('D', 3))
            val testRookData = new RookData(testRook.side, testRook.curPosition.square._1.toString(), testRook.curPosition.square._2.toString())
            socket.send(Json.toJson(testRookData).toString())
            println("tried to send rook data")
        }
        document.getElementById("currentTurn").asInstanceOf[html.Span].innerHTML = turn
        val canvas = document.getElementById("chessCanvas").asInstanceOf[html.Canvas]
        val ctx = canvas.getContext("2d")
        canvas.style.border = "1px solid black"
        //initialize pieces here once I have types and stuff 
        canvas.addEventListener("mousedown", { (e0: dom.Event) =>
         val e = e0.asInstanceOf[dom.MouseEvent] 
         val xy = getCursorPosition(canvas, e)
         val clickedSquare: Position =  new Position((findLetter(xy._1, canvas.width), findNum(xy._2, canvas.height)))
        
        selectedPiece match{
            //if a piece is selected and square is clicked, try to move piece to that square 
            case Some(piece) => { 
                selectedPiece = None
                //make move here 
                if(piece.side.equals(turn)){
               val newBoard = makeMove(piece, clickedSquare)
                
                newBoard match {
                    case Some(newBoard) => {
                    ctx.clearRect(0, 0, canvas.width, canvas.height)
                    board = newBoard
                    drawPieces(canvas, board)
                    switchTurns()
                    document.getElementById("currentTurn").asInstanceOf[html.Span].innerHTML = turn
                    if(isCheckmated(board, turn)){
                        document.getElementById("currentTurn").asInstanceOf[html.Span].innerHTML = "Game over!!"
                        println("checkmated!! game is over")
                    }
                    }
                    case None => println("got none back")
                }
                
                } else println("Attempting to move piece who's turn it isnt ")
            }
            //if no piece is selected, find the piece on clicked square, and select it
            case None => board.map(piece => {
                if(piece.curPosition.equals(clickedSquare)) {
                  
                    selectedPiece = Some(piece)
                }
            })
        }
    })
        //assumes canvas is a square (not square chessboard would be weird)
        //draws squares for board 
       
      board = List(
        new Rook("white", new Position('A', 1)),
        new Knight("white", new Position('B', 1)),
        new Bishop("white", new Position('C', 1)),
        new Queen("white", new Position('D',1)),
        new King("white", new Position('E', 1)),
        new Bishop("white", new Position('F', 1)),
        new Knight("white", new Position('G', 1)), 
        new Rook("white", new Position('H', 1)),
        new Pawn("white", new Position('A', 2)),
        new Pawn("white", new Position('B', 2)),
        new Pawn("white", new Position('C', 2)),
        new Pawn("white", new Position('D', 2)),
        new Pawn("white", new Position('E', 2)),
        new Pawn("white", new Position('F', 2)),
        new Pawn("white", new Position('G', 2)),
        new Pawn("white", new Position('H', 2)),

        new Rook("black", new Position('A', 8)),
        new Knight("black", new Position('B', 8)),
        new Bishop("black", new Position('C', 8)),
        new Queen("black", new Position('D',8)),
        new King("black", new Position('E', 8)),
        new Bishop("black", new Position('F', 8)),
        new Knight("black", new Position('G', 8)), 
        new Rook("black", new Position('H', 8)),
        new Pawn("black", new Position('A', 7)),
        new Pawn("black", new Position('B', 7)),
        new Pawn("black", new Position('C', 7)),
        new Pawn("black", new Position('D', 7)),
        new Pawn("black", new Position('E', 7)),
        new Pawn("black", new Position('F', 7)),
        new Pawn("black", new Position('G', 7)),
        new Pawn("black", new Position('H', 7))
      )
      drawPieces(canvas, board)
      
    }

    def makeMove(piece: Piece, destination: Position): (Option[List[Piece]]) = {
        val previousSquare = piece.curPosition
        var ret: List[Piece] = List.empty[Piece]
            if(piece.validMoves(board).contains(destination)){
                //gets new board if capturing a piece (piece is on the dest square)
                ret = board.filterNot(piece => piece.curPosition.equals(destination)) 
                //moving piece to new destination
                piece.curPosition = destination
                 //for pieces where I care if they've moved yet
                //rooks/kings for castling, pawns for the double move 
                piece match {
                    //this might cause weird behavior if trying to move one of these pieces for the first time
                    //while not in check, and the move wouldn't get you out of check
                    //will run anyways, even tho position would be reset 
                    case pawn: Pawn => pawn.hasMoved = true 
                    case rook: Rook => rook.hasMoved = true 
                    case king: King => king.hasMoved = true
                    case _ => //do nothing 
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
            if(allOpposingMoves.contains(kingPos)){
                false 
            } 
            else true 
          
        }
        def isCheckmated(allPieces: List[Piece], currentPlayer: String): Boolean = {
            val playersPieces = allPieces.filter(piece => piece.side.equals(currentPlayer))
            var checkmated = true 
            playersPieces.map(piece =>{ 
                val initialPosition = piece.curPosition
                piece.validMoves(allPieces).map(candidateMove => {
                val hypoBoard = makeMove(piece, candidateMove)
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
    
}

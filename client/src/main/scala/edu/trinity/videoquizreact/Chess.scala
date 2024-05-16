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


object Chess {

    var board = List.empty[Piece]
    var selectedPiece: Option[Piece] = None
    def runChess() : Unit = {
        println("trying to run chess")
        val canvas = document.getElementById("chessCanvas").asInstanceOf[html.Canvas]
        val ctx = canvas.getContext("2d")
        canvas.style.border = "1px solid black"
        //initialize pieces here once I have types and stuff 
        canvas.addEventListener("mousedown", { (e0: dom.Event) =>
         val e = e0.asInstanceOf[dom.MouseEvent] 
         val xy = getCursorPosition(canvas, e)
         val clickedSquare: Position =  new Position((findLetter(xy._1, canvas.width), findNum(xy._2, canvas.height)))
       // println(clickedSquare)
        selectedPiece match{
            //if a piece is selected and square is clicked, try to move piece to that square 
            case Some(piece) => { makeMove(clickedSquare, piece)
                }
            //if no piece is selected, find the piece on clicked square, and select it
            case None => board.map(piece => if(piece.curPosition.equals(clickedSquare))  selectedPiece = Some(piece))
        }
    })
        //assumes canvas is a square (not square chessboard would be weird)
        //draws squares for board 
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
      val startingBoard:List[Piece] = List(
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
        drawPieces(canvas, startingBoard)
        board = startingBoard //sets global board variable to starting board 
    }
    def makeMove(destination: Position, piece: Piece): Option[List[Piece]] = {
        selectedPiece = None //resets selecged piece S
        println(validMovesWithBlocks(piece))
        if(!piece.validMoves.contains(destination)){
            println("not in piece's valid moves")
            None
        } else println("was in valid moves")
        /*do game logic to determine valid moves
        AM NOT ACCOUNTING FOR PIECES BLOCKING MOVES IN VALID MOVES    
        things to check for:
            -does the new list of pieces contain the piece's sides king in check (will require new function)
            -something along the lines of getting all pieces of the oppisite color then checking if their valid moves has the king's square in it
            but now realizing this is hard with paths not being accounted for in valid moves (king will be in check a lot of times when it's 
            not in reality)
           ::: valid moves might need to change to take the game state into account due to this
            are there any pieces on the moving path (Excluding knights), might be hard
            ^^ will not include destination
            if any piece of any color in the path, is none
            if there is a friendly on the destination, is none
            if there is an enemey on destination, do a capture, return new list of pieces minus the captured piece
            if no pieces on path or destination, and doesn't put your side in check, return new list of pieces with that piece moved
            
            Piece specific things:
            knights will not do a path check
            pawns have to be capturing a piece to move across the x axis 
            pawns hasMoved needs to be false to move 2 squares 
            kings, rooks, pawns hasMoved will be set to true 
            ideally castle? this won't happen till later tho 

        */
        return None
    }
    def validMovesWithBlocks(piece: Piece): List[Position] = {
        val validMovesEmptyBoard:List[Position] = piece.validMoves
        var validMovesWithBlocks = validMovesEmptyBoard
        //will try and filter out moves as it goes 
        val allOccupiedSquares = board.map(thing=>thing.curPosition)
        validMovesEmptyBoard.map(candidateSquare =>{
            if(allOccupiedSquares.contains(candidateSquare)){
                
                piece match {
                    case _: Rook => {
                        if(candidateSquare.square._1 > piece.curPosition.square._1){
                            //piece is blocking to the right
                            println("piece is blocking to the right")
                            validMovesWithBlocks = validMovesEmptyBoard.filter(blockedSquare => blockedSquare.square._1 <= candidateSquare.square._1)
                        } else if(candidateSquare.square._1 < piece.curPosition.square._1){
                            validMovesWithBlocks = validMovesEmptyBoard.filter(blockedSquare => blockedSquare.square._1 > candidateSquare.square._1)
                            
                            println("piece is blocking to the left")
                        } else if(candidateSquare.square._2 > piece.curPosition.square._2){
                            println("piece is blocking above")
                        } else if(candidateSquare.square._2 < piece.curPosition.square._2){
                            println("piece is blocking below")
                        } else println("blocked square is the square the piece is on")
                    }
                }
            }
        })
      validMovesWithBlocks

    }
 
        


    def drawPieces(canvas: html.Canvas, pieces: List[Piece]) = {
        val ctx = canvas.getContext("2d")
        //ideally this can be used to remake the board every time a move is made
        //can also be used for initial board state, and gets passed in all the pieces and their initial positions
        
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
}

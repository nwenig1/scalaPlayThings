package shared


case class Position(square: (Character, Int))

  sealed trait Piece {

    var curPosition: Position
    val side: String
    //validMoves only finds valid, in bounds moves. Does not take game conditions into account
    //ex. doing a move that puts a king in check, or moving a king into check 
    def validMoves: List[Position]
    def inBounds(square: Position): Boolean = {
      if(square.square._1< 'A' || square.square._1 > 'H' || square.square._2 < 1 || square.square._2 > 8) false
        else true 
    }
  }
  class Rook(color: String, startingSquare: Position) extends Piece{  
    val side = color   
    var hasMoved = false //for castling  
    var curPosition = startingSquare
    
    def validMoves: List[Position] = {
      var validMoves= List.empty[Position]
      for(lettSquare <- 'A' to 'H'){ //gets all horizontal moves 
        validMoves ::= new Position((lettSquare, this.curPosition.square._2)) 
      }
      for(numberSquare <- 1 to 8){ //gets all vertical moves 
        validMoves ::= new Position(this.curPosition.square._1, numberSquare)
      }
      //removes square it's currently on 
      validMoves.filterNot(square => square.equals(this.curPosition))
    }

  }
    class King(color: String, startingSquare: Position) extends Piece {
      val side = color
      var hasMoved = false //for castling 
      var curPosition = startingSquare
      def validMoves: List[Position] = {
      var validMoves = List.empty[Position]
      val pos = this.curPosition.square
      //idk how else to do this, there's gotta be a better way 
      validMoves::= new Position(getAdjacentChar(pos._1, 1), pos._2) //directly right
      validMoves::= new Position(getAdjacentChar(pos._1, -1), pos._2) //directly left
      validMoves::= new Position(pos._1, pos._2+1) //directly up
      validMoves::= new Position(getAdjacentChar(pos._1, 1), pos._2-1) //directly down
      validMoves::= new Position(getAdjacentChar(pos._1, 1), pos._2+1) //right/up
      validMoves::= new Position(getAdjacentChar(pos._1, 1), pos._2-1) //right/down
      validMoves::= new Position(getAdjacentChar(pos._1, -1), pos._2+1) //left/up
      validMoves::= new Position(getAdjacentChar(pos._1, -1), pos._2-1) //left/down
      validMoves.filter(move => inBounds(move))
    }

    class Queen(color: String, startingSquare: Position) extends Piece {
      val side = color
      var curPosition: Position = startingSquare
      def validMoves: List[Position] = {
        //once bishop is done, could just make a bishop and rook at the queen's position
        //then queen's valid moves would be those lists combined
        //queen is basically a hybrid rook bishop
        ???
      }
      class Bishop(color: String, startingSquare: Position) extends Piece {
        val side = color
        var curPosition: Position = startingSquare
        def validMoves: List[Position] = {
          ???
        
     

        }
      }


    }

    //returns char with an offset. Ex f('A', 1) gives 'B', f('c', -2) gives 'A'
    //nice helper function if I want to figure out one character to the left/right, whatever 
    def getAdjacentChar(char: Character, offset: Integer): Character ={
          println("get adjacent char with for index " + offset + " and char " + char)
          (char.charValue()-offset).toChar
        
    }

  }



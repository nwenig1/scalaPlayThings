package shared


case class Position(square: (Character, Int))

  sealed trait Piece {

    var curPosition: Position
    val side: String
    //validMoves only finds valid, in bounds moves. Does not take game conditions into account
    //ex. doing a move that puts a king in check, or moving a king into check 
    def validMoves(allPieces: List[Piece]): List[Position]
    def inBounds(square: Position): Boolean = {
      if(square.square._1< 'A' || square.square._1 > 'H' || square.square._2 < 1 || square.square._2 > 8) false
        else true 
    }
     //returns char with an offset. Ex f('A', 1) gives 'B', f('c', -2) gives 'A'
    //nice helper function if I want to figure out one character to the left/right, whatever 
    def getAdjacentChar(char: Character, offset: Integer): Character ={
          (char.charValue()+offset).toChar
    }
  }
  class Rook(color: String, startingSquare: Position) extends Piece{  
    val side = color   
    var hasMoved = false //for castling  
    var curPosition = startingSquare
    
    def validMoves(allPieces: List[Piece]): List[Position] = {
      var validMoves= List.empty[Position]
      var notCollided = true
      //gets all horizontal moves to the left of piece (Starting one to the left)
      //toints are needed so I can go backwards through alphabet and use by -1
      for(leftLettSquare <-((this.curPosition.square._1.toInt) - 1)  to 'A'.toInt by -1){
        //get square attempting to add to
        if(notCollided){
        val candidateSquare = new Position(leftLettSquare.toChar, this.curPosition.square._2)
        //look for a piece with the same position as candidate
        val collisionOpt = allPieces.find(potentialCollision => {potentialCollision.curPosition.equals(candidateSquare)})
        collisionOpt match {
          //if there was a collision
          case Some(conflictingPiece) => {
            //if same side, don't add to valid moves, just turn off path
            if(conflictingPiece.side.equals(this.side)) notCollided = false 
            else {
              //if different sides, add to valid moves
              //(can move to square occupied by opponent to capture)
              validMoves ::= candidateSquare
              notCollided = false
            }
          }
          //if no collisions, add to valid moves, keep looking
          case None => validMoves::=candidateSquare
        }
      } //not collided end 
      } //horizontal left square generator end
      notCollided = true //resets it for next path 
      for(rightLettSquare <- getAdjacentChar(this.curPosition.square._1, 1).toChar to 'H'){
        //get square attempting to add to
        if(notCollided){
        val candidateSquare = new Position(rightLettSquare, this.curPosition.square._2)
        //look for a piece with the same position as candidate
        val collisionOpt = allPieces.find(potentialCollision => {potentialCollision.curPosition.equals(candidateSquare)})
        collisionOpt match {
          //if there was a collision
          case Some(conflictingPiece) => {
            //if same side, don't add to valid moves, just turn off path
            if(conflictingPiece.side.equals(this.side)) notCollided = false 
            else {
              //if different sides, add to valid moves
              //(can move to square occupied by opponent to capture)
              validMoves ::= candidateSquare
              notCollided = false
            }
          }
          //if no collisions, add to valid moves, keep looking
          case None => validMoves::=candidateSquare
        }
      } //not collided end 
      } //horizontal right square generator end

      notCollided = true //resets it for vertical below
      for(downNumSquare <- (this.curPosition.square._2 -1) to 1 by -1){
        if(notCollided){
          val candidateSquare = new Position(this.curPosition.square._1, downNumSquare)
          val collisionOpt = allPieces.find(potentialCollision => {potentialCollision.curPosition.equals(candidateSquare)})
          collisionOpt match {
            case Some(conflictingPiece) => {
               if(conflictingPiece.side.equals(this.side)) notCollided = false 
            else {
              //if different sides, add to valid moves
              //(can move to square occupied by opponent to capture)
              validMoves ::= candidateSquare
              notCollided = false
            }
          }
          //if no collisions, add to valid moves, keep looking
          case None => validMoves::=candidateSquare
        }
          }
        }
        notCollided = true 
        for(upNumSquare <- (this.curPosition.square._2 + 1) to 8){
          if(notCollided){
          val candidateSquare = new Position(this.curPosition.square._1, upNumSquare)
          val collisionOpt = allPieces.find(potentialCollision => {potentialCollision.curPosition.equals(candidateSquare)})
          collisionOpt match {
            case Some(conflictingPiece) => {
               if(conflictingPiece.side.equals(this.side)) notCollided = false 
            else {
              //if different sides, add to valid moves
              //(can move to square occupied by opponent to capture)
              validMoves ::= candidateSquare
              notCollided = false
            }
          }
          //if no collisions, add to valid moves, keep looking
          case None => validMoves::=candidateSquare
        }
          }
        }
          validMoves
        }
        
      }
    
    

  
    class King(color: String, startingSquare: Position) extends Piece {
      val side = color
      var hasMoved = false //for castling 
      var curPosition = startingSquare
      def validMoves(allPieces: List[Piece]): List[Position] = {
        var validMoves = List.empty[Position]
      val moveVetors:Seq[(Int, Int)] = Seq((0, 1), (1, 1), (1, 0), (1, -1), (0, -1), (-1, -1), (-1, 0), (-1, 1))
      moveVetors.map(moveVector =>{
        val candidateMove = new Position(getAdjacentChar(this.curPosition.square._1, moveVector._1), this.curPosition.square._2 + moveVector._2)
        val collisionOpt = allPieces.find(potentialCollision => {potentialCollision.curPosition.equals(candidateMove)})
        collisionOpt match {
          case Some(collision) => if(!collision.side.equals(this.side)) validMoves ::= candidateMove
          case None => validMoves ::= candidateMove
        }
      })
      validMoves.filter(move => inBounds(move))
    }
  }

    class Queen(color: String, startingSquare: Position) extends Piece {
      val side = color
      var curPosition: Position = startingSquare
      def validMoves(allPieces: List[Piece]): List[Position] = {
        //queen's valid moves is just a combination of a bishop's and rooks valid moves
        //makes dummy pieces at the queen's positions and gets valid moves from that 
        new Bishop(this.side, this.curPosition).validMoves(allPieces) ++ new Rook(this.side, this.curPosition).validMoves(allPieces)
      }

    }
      class Bishop(color: String, startingSquare: Position) extends Piece {
        val side = color
        var curPosition: Position = startingSquare
        def validMoves(allPieces: List[Piece]): List[Position] = {
          var validMoves = List.empty[Position]

    // Generating diagonal moves to the top-right
          var (x, y) = this.curPosition.square
          var notCollided = true 
          while (x < 'H' && y < 8 && notCollided) {
            x = getAdjacentChar(x, 1)
            y = y+1 
            val candidateMove = new Position(x, y)
            val collisionOpt = allPieces.find(potentialCollision => {potentialCollision.curPosition.equals(candidateMove)})
            collisionOpt match {
              case Some(conflictingPiece) => {
                notCollided = false 
                if(!conflictingPiece.side.equals(this.side)) validMoves::= candidateMove
            } 
              case None => validMoves ::=candidateMove
            }
          }
          notCollided = true //resets the collision for next path 
        var (x2, y2) = this.curPosition.square
        //gets moves to bottom-right 
        while(x2 < 'H' && y2 > 1 && notCollided){
          x2 = getAdjacentChar(x2, 1)
          y2 = y2-1
          val candidateMove = new Position(x2, y2)
          val collisionOpt = allPieces.find(potentialCollision => {potentialCollision.curPosition.equals(candidateMove)})
           collisionOpt match {
              case Some(conflictingPiece) => {
                notCollided = false 
                if(!conflictingPiece.side.equals(this.side)) validMoves::= candidateMove
            } 
              case None => validMoves ::=candidateMove
            }
          }
        notCollided = true
        var (x3, y3) = this.curPosition.square
        while(x3 > 'A' && y3 < 8 && notCollided){
          x3 = getAdjacentChar(x3, -1)
          y3 = y3+1
          val candidateMove = new Position(x3, y3)
          val collisionOpt = allPieces.find(potentialCollision => {potentialCollision.curPosition.equals(candidateMove)})
           collisionOpt match {
              case Some(conflictingPiece) => {
                notCollided = false 
                if(!conflictingPiece.side.equals(this.side)) validMoves::= candidateMove
            } 
              case None => validMoves ::=candidateMove
            }
        }
        notCollided = true 
        var (x4, y4) = this.curPosition.square
        while(x4 > 'A' && y4 > 1 && notCollided){
          x4 = getAdjacentChar(x4, -1)
          y4 = y4 -1
          val candidateMove = new Position(x4, y4)
          val collisionOpt = allPieces.find(potentialCollision => {potentialCollision.curPosition.equals(candidateMove)})
           collisionOpt match {
              case Some(conflictingPiece) => {
                notCollided = false 
                if(!conflictingPiece.side.equals(this.side)) validMoves::= candidateMove
            } 
              case None => validMoves ::=candidateMove
            }
        }
     validMoves.filter(move=> inBounds(move))
       }
     
      }
      class Pawn(color: String, startingSquare: Position) extends Piece{
        val side = color
        var curPosition: Position = startingSquare
        var hasMoved = false //for first move (can either go one or 2 spots on first move)
        //assumes pawn can capture a piece on move (easier to filter list on game side than grow it)
        def validMoves(allPieces: List[Piece]): List[Position] = {
          var validMoves = List.empty[Position]
        //declaring so can be talked about outside of side match 
          var moveVectors:Seq[(Int, Int)] = Seq.empty[(Int, Int)]
          this.side match {
            case "white" => {
              if(!this.hasMoved) moveVectors =  Seq((0, 1), (1, 1), (-1, 1), (0, 2))
              else moveVectors =  Seq((0, 1), (1, 1), (-1, 1))
            }
            case "black" => {
              if(!this.hasMoved) moveVectors = Seq((0, -1), (1, -1), (-1, -1), (0, -2))
              else moveVectors = Seq((0, -1), (1, -1), (-1, -1))
               
            }
            case _:String=> println("unrecognized color")
          }
          moveVectors.map(moveVector => {
            val candidateMove: Position = new Position(getAdjacentChar(this.curPosition.square._1, moveVector._1), (this.curPosition.square._2 + moveVector._2))
            
            val collisionOpt = allPieces.find(potentialCollision => {potentialCollision.curPosition.equals(candidateMove)})
            collisionOpt match {
              case Some(collision) => {
                //if trying to move sideways onto opposing piece  
                if(moveVector._1 != 0 && (!collision.side.equals(this.side))) {
                  //can move if taking opposing piece
                  validMoves::= candidateMove
                }
              }
              case None => {
                //if no collision, only add it if no sideways movement 
                if(moveVector.equals(0, 1) || moveVector.equals(0, -1)){
             
                 validMoves ::= candidateMove
                //if trying to move 2 spots, makes sure it can move one spot
                //works bc the 2spot move will always happen after the (0, 1) move has been checked for cuz of seq order
                }
               else if((moveVector._2 == 2 || moveVector._2 == -2) && (validMoves.contains((new Position(this.curPosition.square._1, this.curPosition.square._2 +1)))||
                 validMoves.contains(new Position(this.curPosition.square._1, this.curPosition.square._2 -1)))){ 
                  validMoves::= candidateMove
                 }
                }
            }
          })
          validMoves.filter(position => inBounds(position))
        }
      }
      class Knight(color: String, startingSquare: Position) extends Piece {
        val side = color
        var curPosition: Position = startingSquare
        def validMoves(allPieces : List[Piece]): List[Position] = {
          //first is horizontal direction, second is vertical 
          var validMoves = List.empty[Position]
          val moveVectors: Seq[(Int, Int)] = Seq(
            (1, 2), (2, 1), (2, -1), (1, -2), (-1, -2), (-2, -1), (-2, 1),(-1, 2))
          moveVectors.map(moveVector=>{
            val candidatePosition = new Position(getAdjacentChar(this.curPosition.square._1, moveVector._1), this.curPosition.square._2 + moveVector._2)
            val collisionOpt = allPieces.find(potentialCollision => {potentialCollision.curPosition.equals(candidatePosition)})
            collisionOpt match {
              case Some(collision) => if(!collision.side.equals(this.side)) validMoves ::= candidatePosition
              case None => validMoves ::= candidatePosition
            }
          })
          validMoves.filter(square => inBounds(square))
        }
      }
  

      
    

   
  

  



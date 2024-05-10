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


object Chess {
  
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
        println(clickedSquare)
        })
        //assumes canvas is a square (not square chessboard would be weird)
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
        if(y<height* 1/8) 1
        else if(y>height*1/8 && y<height*2/8) 2
        else if(y>height*2/8 && y<height*3/8) 3
        else if(y>height*3/8 && y<height*4/8) 4
        else if(y>height*4/8 && y<height*5/8) 5
        else if(y>height*5/8 && y<height*6/8) 6
        else if(y>height*6/8 && y<height*7/8) 7
        else if(y>height*7/8 && y<height) 8
        else 100 //just for error handling, should never get to this case realistically
        
    }
}

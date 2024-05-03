package edu.trinity.videoquizreact

import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.document
import scala.scalajs.js.annotation.JSExportTopLevel
import slinky.web.svg.circle
object Task11 {
  var shape = "square" 
    def runTask11(): Unit = {
        println("scala js for task 11 running")
        val canvas = document.getElementById("canvas").asInstanceOf[html.Canvas]
        initializePage(canvas)

    }

    def initializePage(canvas: html.Canvas): Unit = {
        //add event listener for square button

        val squareButton = document.getElementById("drawSquare").asInstanceOf[html.Button]
        squareButton.addEventListener("click", { (e0: dom.Event) =>
            shape = "square"
           println("shape changed to " + shape)
        })
        val circleButton = document.getElementById("drawCircle").asInstanceOf[html.Button]
        circleButton.addEventListener("click", {(e0: dom.Event) =>
        shape = "circle"
        println("shape changed to " + shape)
        })
        canvas.addEventListener("mousedown", { (e0: dom.Event) =>
        val ctx = canvas.getContext("2d")
        val e = e0.asInstanceOf[dom.MouseEvent]
        val xyTup = getCursorPosition(canvas, e)
        if(shape == "square"){
        println("trying to draw square")
        ctx.fillRect(xyTup._1, xyTup._2, 50, 50)
        }    
        })
    }
    //give other buttons event listeners here
    




    def getCursorPosition(canvas : html.Canvas, event: dom.MouseEvent): (Int, Int) = {
        val rect = canvas.getBoundingClientRect()
        val x =(event.clientX - rect. left).toInt
        val y = (event.clientY - rect.top).toInt
        (x, y)
    }
}

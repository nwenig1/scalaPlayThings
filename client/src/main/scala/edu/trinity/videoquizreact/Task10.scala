 package edu.trinity.videoquizreact

import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.document
import scala.scalajs.js.annotation.JSExportTopLevel

object Task10 {
    //initial variables, these can be changed to modify initial spawn/ size 
  var rectX = 100
  var rectY = 100
  var score = 0
  val  rectWidth = 100
  val rectHeight = 100

    def runTask10(): Unit = {
        println("scala js for task 10 running")
        val canvas = document.getElementById("hello").asInstanceOf[html.Canvas]
        initializePage(canvas)
        }
        
    def initializePage(canvas: html.Canvas): Unit = {
        val context = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
        canvas.addEventListener("mousedown", { (e0: dom.Event) =>
         val e = e0.asInstanceOf[dom.MouseEvent] 
         val xy = getCursorPosition(canvas, e)
         val mouseX = xy._1
         val mouseY = xy._2
    
        if((mouseX>rectX && mouseX<rectX+rectWidth) && (mouseY>rectY && mouseY < rectY+rectHeight)){ 
            score = score+1 
            val scoreDisplay = document.getElementById("scoreDisplay").asInstanceOf[html.Span]
            scoreDisplay.innerHTML = score.toString
            context.clearRect(0, 0, canvas.width, canvas.height); 
            rectX = (Math.random() * (1000-rectWidth)).toInt
            rectY = (Math.random() * (500 - rectWidth)).toInt
            context.fillRect(rectX, rectY, rectWidth, rectHeight)
        }
          }
    , false)
    //draws initial rectangle 
    context.fillRect(rectX, rectY, rectWidth, rectHeight)
        }

    def getCursorPosition(canvas : html.Canvas, event: dom.MouseEvent): (Int, Int) = {
        val rect = canvas.getBoundingClientRect()
        val x =(event.clientX - rect. left).toInt
        val y = (event.clientY - rect.top).toInt
        (x, y)
    }
}
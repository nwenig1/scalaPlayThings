/* package edu.trinity.videoquizreact

import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.document
import scala.scalajs.js.annotation.JSExportTopLevel

object Task10 {
  val rectX = 0
  val rectY = 0
  val score = 0
  val  rectWidth = 100
  val rectHeight = 100

    def runTask10(): Unit = {
        println("scala js for task 10 running")
        val canvas = document.getElementById("hello").asInstanceOf[html.Canvas]
        initializePage(canvas)
    }
    def initializePage(canvas: html.Canvas): Unit = {
        val context = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
        canvas.addEventListener("click", deleteRect(canvas))
    }
    def deleteRect(canvas: html.Canvas): Unit = {
        
       
    }
}
*/
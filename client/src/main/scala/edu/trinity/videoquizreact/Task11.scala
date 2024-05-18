package edu.trinity.videoquizreact

import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.document
import scala.scalajs.js.annotation.JSExportTopLevel
import slinky.web.svg.circle
import shared.Drawable
import shared.Rectangle
import shared.Circle
import shared.Line
import play.api.libs.json.Json
import scala.collection.mutable
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsError


object Task11 {
    implicit val rectangleWrites: play.api.libs.json.OWrites[shared.Rectangle] = Json.writes[Rectangle]
    implicit val lineWrites: play.api.libs.json.OWrites[shared.Line] = Json.writes[Line]
    implicit val circleWrites: play.api.libs.json.OWrites[shared.Circle] = Json.writes[Circle]
    implicit val rectanglereads: play.api.libs.json.Reads[shared.Rectangle] = Json.reads[Rectangle]
    implicit val linereads: play.api.libs.json.Reads[shared.Line] = Json.reads[Line]
    implicit val circlereads: play.api.libs.json.Reads[shared.Circle] = Json.reads[Circle]
    implicit val drawableReads: play.api.libs.json.Reads[shared.Drawable] = Json.reads[Drawable]
    var shape = "square" //starts off as square initially 
    var color= "black"
    var linePoint1 : Option[(Int, Int)] = None
    var socketRoute = document.getElementById("ws-route").asInstanceOf[html.Input].value
    var socket = new dom.WebSocket(socketRoute.replace("http", "ws"))
    socket.onopen = (event) => socket.send(Json.toJson("New user connected").toString())
    socket.onmessage = (event) => {
    Json.fromJson[List[Drawable]](Json.parse(event.data.toString())) match {
        case JsSuccess(shapes, _) => drawShapes(shapes)
        case JsError(error) => println("Issue w/ shape parsing")
    }
  }
    
    def runTask11(): Unit = {
        println("scala js for task 11 running")
        val canvas = document.getElementById("canvas").asInstanceOf[html.Canvas]
        initializePage(canvas)
    }

    def initializePage(canvas: html.Canvas): Unit = {
        canvas.style.border = "1px solid black"
        //adds event listener to select desired shapes 
        val squareButton = document.getElementById("drawSquare").asInstanceOf[html.Button]
        squareButton.addEventListener("click", { (e0: dom.Event) =>
            shape = "square"
        })
        val circleButton = document.getElementById("drawCircle").asInstanceOf[html.Button]
        circleButton.addEventListener("click", {(e0: dom.Event) =>
        shape = "circle"
        })
         val lineButton = document.getElementById("drawLine").asInstanceOf[html.Button]
        lineButton.addEventListener("click", {(e0:dom.Event) =>
            shape="line"
        })
        //adds event listeners for colors
        val blackButton = document.getElementById("black").asInstanceOf[html.Button]
        blackButton.addEventListener("click", {(e0: dom.Event) =>
            color = "black"
        })
        val blueButton = document.getElementById("blue").asInstanceOf[html.Button]
        blueButton.addEventListener("click", {(e0: dom.Event) =>
            color = "blue"
        })
        val redButton = document.getElementById("red").asInstanceOf[html.Button]
        redButton.addEventListener("click", {(e0: dom.Event) =>
            color = "red"
        })
        val orangeButton = document.getElementById("orange").asInstanceOf[html.Button]
        orangeButton.addEventListener("click", {(e0: dom.Event) =>
            color = "orange"
        })

        //determines which new shape to send to the server
        canvas.addEventListener("mousedown", { (e0: dom.Event) =>
        val ctx = canvas.getContext("2d")
        val e = e0.asInstanceOf[dom.MouseEvent]
        val xyTup = getCursorPosition(canvas, e)
        if(shape == "square"){
            socket.send(Json.toJson(new Rectangle(xyTup._1-25, xyTup._2-25, color)).toString())
        }else if(shape == "line"){
            linePoint1 match {
                //if first location has been clicked, send it along with the newly clicked 2nd point
                case Some(linePoint) => { 
                    socket.send(Json.toJson(new Line(linePoint._1, linePoint._2, xyTup._1, xyTup._2, color)).toString())
                    //resets the first line point for future lines
                    linePoint1 = None
                }
                //if first location hasn't been clicked, set it to what was clicked
                case None => {
                    linePoint1 = Some(xyTup)
                }
            }
        } else if(shape == "circle"){
            socket.send(Json.toJson(new Circle(xyTup._1, xyTup._2, 50, color)).toString())
        }    
    })
}   

    def drawShapes(drawings: List[Drawable]): Unit = {
        val canvas = document.getElementById("canvas").asInstanceOf[html.Canvas]
        drawings.map { drawing =>
            drawing match {
                case Rectangle(x, y, color) => drawRect(canvas, x, y, color)
                case Circle(cX, cY, radius, color) => drawCircle(canvas, cX, cY, radius, color)
                case Line(x1, y1, x2, y2, color) => drawLine(canvas, x1, y1, x2, y2, color)
            }
        }
    }
    def drawRect(canvas: html.Canvas, x: Int, y: Int, color: String): Unit = {
        val ctx=canvas.getContext("2d")
        ctx.fillStyle = color
        ctx.fillRect(x, y, 50, 50)
    }
    def drawCircle(canvas: html.Canvas, cX: Int, cY: Int,radius: Double, color: String): Unit = {
        val ctx=canvas.getContext("2d")
        ctx.beginPath()
        ctx.arc(cX, cY, radius, 0, 2 * Math.PI) // x, y, radius, startAngle, endAngle
        ctx.strokeStyle = color
        ctx.lineWidth = 2
        ctx.stroke()
    }
    def drawLine(canvas: html.Canvas, x1: Int, y1: Int, x2: Int, y2: Int, color:String ){
        val ctx=canvas.getContext("2d")
        ctx.beginPath()
        ctx.moveTo(x1, y1) 
        ctx.lineTo(x2, y2) 
        ctx.strokeStyle = color
        ctx.lineWidth = 2
        ctx.stroke()
    }


    def getCursorPosition(canvas : html.Canvas, event: dom.MouseEvent): (Int, Int) = {
        val rect = canvas.getBoundingClientRect()
        val x =(event.clientX - rect. left).toInt
        val y = (event.clientY - rect.top).toInt
        (x, y)
    }
}

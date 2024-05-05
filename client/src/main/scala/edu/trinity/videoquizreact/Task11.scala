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


object Task11 {
  var shape = "square" 
  var linePoint1 : Option[(Int, Int)] = None
  var socketRoute = document.getElementById("ws-route").asInstanceOf[html.Input].value
  var socket = new dom.WebSocket(socketRoute.replace("http", "ws"));
  socket.onopen = (event) => socket.send(Json.toJson("New user connected").toString())

  

      implicit val rectangleWrites = Json.writes[Rectangle]
      implicit val lineWrites = Json.writes[Line]
      implicit val circleWrites = Json.writes[Circle]

      implicit val rectanglereads = Json.reads[Rectangle]
      implicit val linereads = Json.reads[Line]
      implicit val circlereads = Json.reads[Circle]
      implicit val drawableReads = Json.reads[Drawable]
      
      //below is what I'd like to do in an ideal world
      //implicit val drawableReads = Json.reads[List[Drawable]]

    
    def runTask11(): Unit = {
        println("scala js for task 11 running")
        println(socketRoute)
        val canvas = document.getElementById("canvas").asInstanceOf[html.Canvas]
        initializePage(canvas)

    }

    def initializePage(canvas: html.Canvas): Unit = {
   
        canvas.style.border = "1px solid black"
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
         val lineButton = document.getElementById("drawLine").asInstanceOf[html.Button]
        lineButton.addEventListener("click", {(e0:dom.Event) =>
            shape="line"
        println("shape changed to " + shape)
        })

        //this is what actually draws it 
        canvas.addEventListener("mousedown", { (e0: dom.Event) =>
        val ctx = canvas.getContext("2d")
        val e = e0.asInstanceOf[dom.MouseEvent]
        val xyTup = getCursorPosition(canvas, e)
        if(shape == "square"){
        //here is where it would send the data to the server
        println("on client sending: " + new Rectangle(xyTup._1, xyTup._2).toString())
        socket.send(Json.toJson(new Rectangle(xyTup._1, xyTup._2)).toString())
        //ctx.fillRect(xyTup._1, xyTup._2, 50, 50)
        }else if(shape == "line"){
            linePoint1 match {
                case Some(linePoint) => { 
                    socket.send(Json.toJson(new Line(linePoint._1, linePoint._2, xyTup._1, xyTup._2)).toString())
                   // ctx.beginPath()
                   // ctx.moveTo(linePoint._1, linePoint._2) 
                   // ctx.lineTo(xyTup._1, xyTup._2) 
                   // ctx.strokeStyle = "black"
                   // ctx.lineWidth = 2
                   // ctx.stroke()
                    linePoint1 = None
                }
                case None => {
                    linePoint1 = Some(xyTup)
                }
            }
        } else if(shape == "circle"){
            println("client is sending out : " + new Circle(xyTup._1, xyTup._2, 50)).toString()
            socket.send(Json.toJson(new Circle(xyTup._1, xyTup._2, 50)).toString())
           // ctx.beginPath()
           // ctx.arc(xyTup._1, xyTup._2, 50, 0, 2 * Math.PI) // x, y, radius, startAngle, endAngle
           // ctx.strokeStyle = "black"
           // ctx.lineWidth = 2
           // ctx.stroke()
        }    
        })
        
    }   
    socket.onmessage = (event) => {
    println("rawData" + event.data)
    val parsedData = Json.parse(event.data.toString())
    println("parsedData: " + parsedData)
    val fromJsonData = Json.fromJson[Drawable](parsedData)
    println("fromJsonData: " + fromJsonData)

    

    
  

   

  }
 
  //def processShapes(input: String): mutable.Seq[Drawable] = {
  //  val ret = mutable.Seq[Drawable]()
//
  //  ret 
  //}
 


    def getCursorPosition(canvas : html.Canvas, event: dom.MouseEvent): (Int, Int) = {
        val rect = canvas.getBoundingClientRect()
        val x =(event.clientX - rect. left).toInt
        val y = (event.clientY - rect.top).toInt
        (x, y)
    }
}

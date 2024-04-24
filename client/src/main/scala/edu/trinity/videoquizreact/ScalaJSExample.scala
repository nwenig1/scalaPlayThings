package edu.trinity.videoquizreact

import shared.SharedMessages
import org.scalajs.dom

import slinky.core._
import slinky.web.ReactDOM
import slinky.web.html._

object ScalaJSExample {

  def main(args: Array[String]): Unit = {
    println("main running")
    if(dom.document.getElementById("task10") != null){

      println("task 10 found")
     Task10.runTask10()
    }
    // This line demonstrates using Scala.js to modify the DOM.
    if(dom.document.getElementById("scalajsShoutOut") != null){
    dom.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
    
    // What is below is using Scala.js with Slinky to use React.
    println("Call the react stuff.")
    ReactDOM.render(
      div(
        h1("Hello, world!"),
        p("This is a component added with Slinky, a Scala.js React binding.")
      ),
      dom.document.getElementById("root")
    )
  }
}
}

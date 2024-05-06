package shared

sealed trait Drawable 

case class Rectangle(x: Int, y:Int, color: String) extends Drawable
case class Line(x1:Int, y1:Int, x2:Int, y2:Int, color: String) extends Drawable
case class Circle(cX: Int, cY: Int, radius: Double, color: String) extends Drawable




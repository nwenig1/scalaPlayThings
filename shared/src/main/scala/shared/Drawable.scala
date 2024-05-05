package shared

sealed trait Drawable 

case class Rectangle(x: Int, y:Int) extends Drawable
case class Line(x1:Int, y1:Int, x2:Int, y2:Int) extends Drawable

case class Circle(cX: Int, cY: Int, radius: Double) extends Drawable




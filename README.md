# play-scalajs-slinky

This was the repository for my Web Application Developement class from Spring of 2024. Was my first web development experience, and was in the scala play framework. Server can be run locally on port 9000 with the command "sbt run" while in the repository. 

Routes:
/task5 : Shared message board with global messages, and the option to send one to a specific user. Messages and user info are stored with an in-memory model
/task7 : connecting to the page spawns a sprite, which can move around. All people connected to the page can see all sprites update in real time. Can connect with multiple pages to see this
/task8 : Redo of task 5, but now using React components. 
/task9 : Redo of task 5, using a databse. I leave the db off by default, so likely will not work
/task10 : Whac-A-Mole game using ScalaJS instead of javascript for the client side. 
/task11 : Shared drawing palette, all people connected to the page can draw and see updates in real time. Can connect with multiple pages to see this

All the above were assignments for the course. The summer following, I added one more page as a side project:

/chess : Chess game!! 2 players can join the page and make moves, seeing those updates in real time. Most chess features are there (checks, checkmate, etc.) execept for pawn promotions and castling. The first player to connect is white, the second is black, any others are spectators. Only white/black can move their respective sides on their turn, but spectators can reset the game whenever. 


Scala Build Tool (sbt) download: https://www.scala-sbt.org/download/ 




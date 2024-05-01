console.log("js for task 7 running")

const socketRoute = document.getElementById("ws-route").value; 

const socket = new WebSocket(socketRoute.replace("http", "ws")); 

socket.onopen = (event) => socket.send("New User connected"); 

const canvas = document.getElementById("canvas"); 
const ctx = canvas.getContext('2d'); 
document.addEventListener("keydown", function(event){
     if(event.key === "ArrowUp"){
        console.log("up arrow pressed"); 
     } else if(event.key === "ArrowLeft"){
        console.log("left arrow pressed"); 
     } else if(event.key === "ArrowRight"){
        console.log("right arrow pressed"); 
     }else if(event.key === "ArrowDown"){
        console.log("down arrow pressed"); 
     }

}); 




console.log("js for task 7 running")



const socketRoute = document.getElementById("ws-route").value; 

const socket = new WebSocket(socketRoute.replace("http", "ws")); 



const canvas = document.getElementById("canvas"); 
const ctx = canvas.getContext('2d'); 

const height = window.innerHeight;
const width = window.innerWidth;

canvas.width = width;
canvas.height = height;

let myself = {color: "black", posX: width/2, posY: height/2};

let allSprites = [];
document.addEventListener("keydown", function(event){
     if(event.key === "ArrowUp"){
         socket.send([myself.posX, myself.posY -= 10])
     } else if(event.key === "ArrowLeft"){
        socket.send([myself.posX-=10, myself.posY])
     } else if(event.key === "ArrowRight"){ 
        socket.send([myself.posX+=10, myself.posY])
     }else if(event.key === "ArrowDown"){
        socket.send([myself.posX, myself.posY += 10])
     }
}); 

socket.onopen = (event) => socket.send([myself.posX, myself.posY]); 


socket.onmessage = (event) =>{
const actualData = JSON.parse(event.data.replaceAll('"','')); 

redrawRectangles(actualData)


}

function redrawRectangles(locations){
   ctx.clearRect(0, 0, canvas.width, canvas.height)
   for(let i=0; i< locations.length; i++){

      ctx.beginPath(); 
      ctx.fillStyle="black"; 
      ctx.stroke(); 
      ctx.fillRect(locations[i][0], locations[i][1], 50, 50)
      ctx.closePath(); 
   }


}
 







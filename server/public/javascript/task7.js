console.log("js for task 7 running")

const socketRoute = document.getElementById("ws-route").ariaValueMax; 

const socket = new WebSocket(socketRoute.replace("http", "ws")); 

socket.onopen = (event) => socket.send("New User connected"); 


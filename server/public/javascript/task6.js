
const canvas = document.getElementById("sprite-canvas"); 

const socketRoute = document.getElementById("ws-route").value;
const socket = new WebSocket(socketRoute.replace("http", "ws")); 


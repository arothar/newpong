var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = [];
var balls = [];


server.listen(8080, function(){
	console.log("El Servidor esta corriendo...");
});

io.on('connection', function(socket){
	console.log("Jugador Conectado!");
	socket.emit('socketID', { id: socket.id });
	socket.emit('getPlayers', players);
	socket.broadcast.emit('newPlayer', {id: socket.id});
	for(var i = 0; i < players.length; i++){
		console.log("Jugador: " + players[i].id);
	}
	socket.on('playerMoved', function(data){
		data.id = socket.id;
		socket.broadcast.emit('playerMoved', data);					
		//console.log("player Moved ID: " + data.id + " X" + data.x + " Y" + data.y);	
		for (var i = 0; i < players.length; i++){
			if (players[i].id == data.id) {
				players[i].x = data.x;
				players[i].y = data.y;
			}
		}
	});	
	socket.on('ballMoved', function(data){
		socket.broadcast.emit('ballMoved', data);		
		//console.log("ball moved" + "X" + data.x + "Y" + data.y);		
		for (var i = 0; i < players.length; i++){				
				ball.id = "ball";
				ball.x = data.x;
				ball.y = data.y;			
		}
	});
	
	socket.on('disconnect', function(){
		console.log("Jugador Desconectado");
		socket.broadcast.emit('playerDisconnected', { id: socket.id});
		for (var i = 0; i < players.length; i++){
			if (players[i].id == socket.id){
				players.splice(i,1);
			}
		}
	});	
	players.push(new player(socket.id,0,0));
	balls.push(new ball(ball.id, ball.x, ball.y));
});

function player(id, x, y){
	this.id = id;
	this.x = x;
	this.y = y;
}
function ball (x, y){
	this.id = "ball";
	this.x = x;
	this.y = y;
}
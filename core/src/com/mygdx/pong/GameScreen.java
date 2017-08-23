package com.mygdx.pong;

import java.util.HashMap;
import java.util.Random;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import Elements.Ball;
import Elements.Paddle;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class GameScreen extends AbstractScreen {

	private SpriteBatch batch;
	private Texture texture;
	private Paddle player1;
	private Paddle player2;
	private Ball ball;
	private int paddlePos;
	private BitmapFont score1, score2;
	public int firstScore = 0, secondScore = 0;
	private Main Main;
	private Socket socket;
	private HashMap<String, Paddle> playerMap;
	private HashMap<String, Ball> ballMap;
	public boolean status1 = false, status2 = false;
	
	public GameScreen(Main main) {
		super(main);
		Main = main;	
		Main.menuScreen.dispose();
	}
		
	@Override
	public void show(){
		batch = new SpriteBatch();
		texture = new Texture(Gdx.files.internal("Medio.png"));		
		score1 = score2 =  new BitmapFont();//Gdx.files.internal("ubuntu.fnt"),Gdx.files.internal("ubuntu.png"), false);
		//playerAwait = new BitmapFont(Gdx.files.internal("ubuntu.fnt"), Gdx.files.internal("ubuntu.png"), false);
		playerMap = new HashMap<String, Paddle>();
		ballMap = new HashMap<String, Ball>();
		ball = new Ball();
		connectSocket();
		configSocketEvents();		
		player1 = new Paddle();
		//player2 = new Paddle();
		//player2.setPosX(780);
	}
	
	@Override
	public void render(float delta){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (player1.id != null) {
				player1.update(); 
			}		

		updatePlayers();
		if (player1 != null && player2 != null) {
			ball.update(player1, player2);
			updateBall();	
		}	
		
		batch.begin();
		batch.draw(texture, 0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if (player1 != null){
			player1.drawPaddle(batch);
		}
		for (HashMap.Entry<String, Paddle> entry : playerMap.entrySet()){
			entry.getValue().drawPaddle(batch);
		}
		for (HashMap.Entry<String, Ball> entryBall : ballMap.entrySet()){
			entryBall.getValue().drawBall(batch);
		}
		ball.drawBall(batch);
		score1.draw(batch, "" + Integer.toString(firstScore), 180 ,Gdx.graphics.getHeight() - 10);
		score2.draw(batch, "" + Integer.toString(secondScore), 600 ,Gdx.graphics.getHeight() - 10);
		batch.end(); 
		updateScore();
		checkScore();		
	}
	
	public void connectSocket(){
		try {
			socket = IO.socket("http://localhost:8080");
			socket.connect();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	@SuppressWarnings("unused")
	private int generatePaddlePosition(){
		Random rn = new Random();
		paddlePos = rn.nextInt(400);
		if ( paddlePos <= 0)
			paddlePos += 100;
		
		return paddlePos;
	}
	
	private void updateScore(){
		if (ball.getBall().x > Gdx.graphics.getWidth() - 1)
			firstScore += 1;
		else if (ball.getBall().x < 1)
			secondScore += 1;
	}
	
	public void updatePlayers(){
		JSONObject data = new JSONObject();
		try {
			data.put("x", player1.getPos().x);
			data.put("y", player1.getPos().y);
			socket.emit("playerMoved", data);
		} catch (JSONException e){
			Gdx.app.log("SOCKET.IO", "Error sending update data.");
		}
	}
	
	public void updateBall(){
		JSONObject data = new JSONObject();
		try {
			data.put("x", ball.getPosX());
			data.put("y", ball.getPosY());
			socket.emit("ballMoved", data);
		} catch (JSONException e){
			Gdx.app.log("SOCKET.IO", "Error sending ball data.");
		}
	}
	
	private void checkScore(){
		String Ganador = null;
		if (firstScore >= 5) Ganador = player1.name;
		if (secondScore >= 5) Ganador = player2.name;
		if (Ganador != null) {JOptionPane.showMessageDialog(null,"Ganador: " + Ganador); Gdx.app.exit();}
	}	
	
	public void configSocketEvents(){
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {			
			@Override
			public void call(Object... args) {
				Gdx.app.log("SocketIO", "Connected");
			}
		}).on("socketID", new Emitter.Listener() {			
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String id = data.getString("id");
						playerMap.put(id, new Paddle());
						player1.setName("Jugador1");
						player1.setID(id);
						status1 = true;		
					Gdx.app.log("SocketIO", "My ID: " + player1.id);	
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting ID" + e);
				}	
			}
		}).on("newPlayer", new Emitter.Listener() {			
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String id = data.getString("id");	
					Gdx.app.log("SocketIO", "se conecto un jugador");
												
						status2 = true;
					Gdx.app.log("SocketIO", "Second Player Connected: " + id);					
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting Second Player ID" + e);
				}	
			}
		}).on("playerDisconnected", new Emitter.Listener() {		
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String id = data.getString("id");
					playerMap.remove(id);
					status1 = false;
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting New Player ID" + e);
				} 	
			}
		}).on("getPlayers", new Emitter.Listener() {			
			@Override
			public void call(Object... args) {
				JSONArray objects = (JSONArray) args[0];
				try{
					for (int i = 0; i < objects.length(); i++){
						Paddle enemyPlayer = new Paddle();
						Vector2 pos = new Vector2();
						pos.x = enemyPlayer.getPos().x;
						pos.y = enemyPlayer.getPos().y;						
						playerMap.put(objects.getJSONObject(i).getString("id"), enemyPlayer);
						enemyPlayer.setPosX(780);
						player2 = enemyPlayer;
						player2.setID(objects.getJSONObject(i).getString("id"));
						if (status1 && status2){
							playerMap.get(player1.id).setPosX(0);
							playerMap.get(player2.id).setPosX(780);
						}
					}
				} catch (JSONException e){
				}
			}
		}).on("playerMoved", new Emitter.Listener() {			
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String playerId = data.getString("id");
					Double y = data.getDouble("y");
					if (playerMap.get(playerId) != null) {
						playerMap.get(playerId).setPos(y.floatValue());						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}	
			}
		}).on("ballMoved", new Emitter.Listener() {			
			@Override
			public void call(Object... args) {
				JSONObject object = (JSONObject) args[0];
					try {
						String posX = object.getString("x");	
						String posY = object.getString("y");
						ball.setPos(Float.parseFloat(posX), Float.parseFloat(posY));
						if (player1 != null && player2 != null)	{
							ball.checkCollide(ball, player1, player2);
						}						
					} catch (JSONException e) {
						e.printStackTrace();
					}				
				}
		});
	}
}

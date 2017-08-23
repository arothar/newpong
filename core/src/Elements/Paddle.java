package Elements;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.pong.GameScreen;


public class Paddle {
	protected Texture texture;
	protected Rectangle rectangle;
	protected float width = 20;
	protected float height = 100;
	public String name, id;
	public int speed = 5;
	public float posx,posy;
	private int paddlePos;
	
	public Paddle(){
		//texture = new Texture("Raqueta.png");
		rectangle = new Rectangle(0,0, width, height);
	}
	
	public String getId(){
		return id;
	}

	public void drawPaddle(SpriteBatch batch){
		texture = new Texture("Raqueta.png");
		batch.draw(texture, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void update(){
		if(Gdx.input.isKeyPressed(Keys.W)){
			if(rectangle.y < Gdx.graphics.getHeight() - rectangle.getHeight())
			rectangle.y += speed;
		}
		if(Gdx.input.isKeyPressed(Keys.S)){
			if (rectangle.y > 0)
			rectangle.y -= speed;
		}
	}
	
	public void setID(String id){
		this.id = id;
	}
	
	public Rectangle getPos(){
		return rectangle;
	}
	
	public String getID (){
		return id;
	}
	
	public Paddle getPaddle(){
		return this;
	}
	
	public void setPos(float y){
		this.rectangle.y = y;
	}	
	
	public void setPosX(float x){
		this.rectangle.x = x;
	}
	private int generatePaddlePosition(){
		Random rn = new Random();
		paddlePos = rn.nextInt(400);
		if ( paddlePos <= 0)
			paddlePos += 100;		
		return paddlePos;
	}
}

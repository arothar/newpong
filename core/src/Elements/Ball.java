package Elements;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import Elements.Paddle;
import com.mygdx.pong.GameScreen;

public class Ball {
	
	private static float width = 20;
	private static float height = 20;
	private static float speed = 4;
	private Texture texture;
	private Rectangle rectangle;
	private int xpos, ypos, xdir, ydir;
	private int xposOriginal = 400;
    private int yposOriginal = 250;
	
	public Ball(){		
		generatePosition();
		rectangle = new Rectangle(xpos,ypos,width, height);
		texture = new Texture(Gdx.files.internal("Ball.png"));
		xdir = ydir = 1;
	}
	
	public void drawBall(SpriteBatch batch){
		batch.draw(texture, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
	}
	
	public void generatePosition(){
		Random rn = new Random();
		xpos = rn.nextInt(750);
		ypos = rn.nextInt(450);
		if (xpos < 20)
			xpos +=20;
		if (ypos < 20)
			ypos +=20;
	}
	
	public void update(Paddle p1, Paddle p2){
		if (PaddleCollitioner(p1.getPos(),p2.getPos())){
			xdir = xdir * -1;
		}
		if (WallCollitioner()){
			ydir = ydir * - 1;
		}
		
		rectangle.x = rectangle.x + speed * xdir;
		rectangle.y = rectangle.y + speed * ydir;
		
		checkBallPos();
	}
	
	public Rectangle getBall(){
		return rectangle;
	}
	
	private Boolean PaddleCollitioner(Rectangle p1, Rectangle p2){
		if (rectangle.overlaps(p1)){
			rectangle.x = p1.x + p1.getWidth();
			return true;
		}
		if (rectangle.overlaps(p2)){
			rectangle.x = p2.x - p2.getWidth();
			return true;
		}			
		return false;
	}
	private boolean WallCollitioner() {
		if(rectangle.y + texture.getHeight() >= Gdx.graphics.getHeight()) { 
			rectangle.y = Gdx.graphics.getHeight() - texture.getHeight();
			return true;
		}
		else if(rectangle.y <= 0) {
			rectangle.y = 0;
			return true;
		}
		else return false;
	}
	
	public void checkBallPos(){		
		if (rectangle.x < 0 || rectangle.x > Gdx.graphics.getWidth()){
			setStartingPos();
		}	
	}
	
	private void setStartingPos(){
		rectangle.x = xposOriginal;
		rectangle.y = yposOriginal;
	}
	
	public float getSpeed(){
		return speed;
	}
	
	@SuppressWarnings("static-access")
	public void setSpeed(float incSpeed){
		this.speed = incSpeed;
	}
	
	public float getPosX(){
		return rectangle.x;
	}
	
	public float getPosY(){
		return rectangle.y;
	}
	
	public void setPos(float x, float y){
		rectangle.x = x;
		rectangle.y = y;
		checkBallPos();
	}
	
	public Boolean checkCollide(Ball ball, Paddle player1, Paddle player2){
		if (WallCollitioner()){ return true;}
		if (PaddleCollitioner(player1.getPos(),player2.getPos())){ return true;}
		return false;
	}
}

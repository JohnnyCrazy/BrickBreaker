package powerups;

import gamestates.Ingame;
import main.Ball;
import main.CountdownWatch;
import main.Player;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.StateBasedGame;


public class Bomb extends Powerup
{
	
	Image image_big;
	Image image_small;
	
	long time = 1000;
	
	Circle hitbox;
	Ingame ing;
	CountdownWatch watch;
	
	float x;
	float y;
	
	//Gibt an, ob das Powerup momentan runterf�llt, oder ob es schon aufgefangen wurde
	Boolean dropping;
	
	public Bomb()
	{		
		x = 0;
		y = 0;		
		hitbox = new Circle(x, y, 25);
		watch = new CountdownWatch(time);
	}
	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return "Bomb";
	}
	@Override
	public long getTime() 
	{
		return time;
	}
	@Override
	public boolean isDropping() 
	{
		return dropping;
	}
	@Override
	public void setDropping(Boolean dropping) 
	{
		this.dropping = dropping;
	}
	@Override
	public Image getSmallImage() 
	{
		return image_small;
	}
	@Override
	public CountdownWatch getCountdown() 
	{
		return watch;
	}
	@Override
	public Circle getHitbox()
	{
		return hitbox;
	}
	
	@Override
	public void setX(float x)
	{
		this.x = x;
		hitbox.setX(x);
	}

	@Override
	public void setY(float y) {
		this.y = y;
		hitbox.setY(y);
		
	}

	@Override
	public float getX() 
	{
		return x;
	}

	@Override
	public float getY()
	{
		return y;
	}

	@Override
	public void drop(Ingame ing) {
		this.ing = ing;
		dropping = true;
		image_big = null;
		try {
			image_big = new Image("./resources/powerup_bombe.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		image_small = image_big.getScaledCopy(0.5F);
	}
	@Override
	public void onActivate() 
	{
		hitbox.setLocation(0, 0);
		Player ply = ing.getPlayer();
		Ball ball = ing.getBall();
		if(ply.getLives() > 1)
		{
			ply.setLives(ply.getLives() - 1);
			ball.setX(10);
			ball.setY(400);
			ball.setVec(1F, 1F);
			Mouse.setCursorPosition(90, Mouse.getY());
			ply.getShape().setX(90);
			ing.pause();
		}
		else
		{
			ply.setLives(ply.getLives() - 1);
			ing.endGame(false);
		}
	}
	@Override
	public void onDeactivate() 
	{
		
	}
	@Override
	public void onRender(GameContainer gc, StateBasedGame sb, Graphics g) 
	{
		if(dropping)
		{
			g.drawImage(image_big, x, y );
		}
		else
		{
			
		}
	}

	@Override
	public void onUpdate(GameContainer gc, StateBasedGame sb, int delta) 
	{
		if(dropping)
		{
			float hip = 0.19F * delta;
			setY(getY() + hip);
		}
		else
		{
			System.out.println(watch.getTime());
		}
	}
}

package powerups;

import gamestates.Ingame;
import main.CountdownWatch;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.StateBasedGame;

//Powerup Superklasse
public abstract class Powerup {
	

	
	public abstract String getName();
	
	public abstract Circle getHitbox();
	public abstract boolean isDropping();
	//Fällt es oder ist es aktiviert?
	public abstract void setDropping(Boolean dropping);
	public abstract Image getSmallImage();
	public abstract CountdownWatch getCountdown();
	public abstract long getTime();
	
	public abstract void setX(float x);
	public abstract void setY(float y);
	public abstract float getX();
	public abstract float getY();
	
	public abstract void drop(Ingame ing);
	public abstract void onActivate();
	public abstract void onDeactivate();
	
	public abstract void onRender(GameContainer gc, StateBasedGame sb, Graphics g);
	public abstract void onUpdate(GameContainer gc, StateBasedGame sb, int delta);

}

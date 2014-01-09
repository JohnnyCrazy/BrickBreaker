package powerups;

import gamestates.Ingame;
import main.CountdownWatch;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.StateBasedGame;


public class None extends Powerup {

	
	
	public None()
	{
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "NONE";
	}

	@Override
	public Circle getHitbox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void drop(Ingame ing) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRender(GameContainer gc, StateBasedGame sb, Graphics g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate(GameContainer gc, StateBasedGame sb, int delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setX(float x) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setY(float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isDropping() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDropping(Boolean dropping) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Image getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CountdownWatch getCountdown() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onActivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeactivate() {
		// TODO Auto-generated method stub
		
	}
}

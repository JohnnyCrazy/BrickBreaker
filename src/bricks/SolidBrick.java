package bricks;

import java.util.ArrayList;

import main.Ball;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import powerups.None;
import powerups.Powerup;

//SolidBrick Klasse, Block der nicht zerstört werden kann
public class SolidBrick extends Brick{
	Rectangle brick;
	Powerup powerup;
	
	public SolidBrick(int x,int y,int width,int height)
	{
		brick = new Rectangle(x, y, width, height);
		powerup = new None();
	}
	public Shape getShape() 
	{
		return brick;
	}
	public Color getColor()
	{
		return Color.gray;
	}
	public void onHit()
	{
		return;
	}
	public void setPowerup(Powerup power)
	{
		
	}
	public Powerup getpowerup() 
	{
		return powerup;
	}
	@Override
	public void onHit(Ball ball, int ID, ArrayList<Brick> bricks,ArrayList<Integer> direc) 
	{
		//Mach nichts mit dem Brick, da er ja nicht zerstört werden kann
		if(direc.size() == 1)
		{
			switch(direc.get(0)){
			
			case 0:
				ball.setVec(ball.getVec().x, -ball.getVec().y);
				break;
			case 1:
				ball.setVec(ball.getVec().x, -ball.getVec().y);
				break;
			case 2:
				ball.setVec(-ball.getVec().x,ball.getVec().y);
				break;
			case 3:
				ball.setVec(-ball.getVec().x,ball.getVec().y);
				break;
			}
		}
		else if(direc.size() == 2)
		{
			
			ball.setVec(-ball.getVec().x,-ball.getVec().y);
		}
		else
		{
			new Exception("Error in collision detection");
		}
	}
	@Override
	public String getName()
	{
		return "SOLID";
	}
}
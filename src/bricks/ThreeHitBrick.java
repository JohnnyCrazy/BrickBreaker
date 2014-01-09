package bricks;

import java.util.ArrayList;

import main.Ball;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import powerups.Powerup;

//Klasse für den 3HIT Brick
public class ThreeHitBrick extends Brick{
	Rectangle brick;
	Powerup powerup;
	
	public ThreeHitBrick(int x,int y,int width,int height)
	{
		brick = new Rectangle(x, y, width, height);
	}
	public Shape getShape() 
	{
		return brick;
	}
	public Color getColor()
	{
		return Color.red;
	}
	public void onHit()
	{
		return;
	}
	public void setColor(Color color) 
	{
		// TODO Auto-generated method stub
		
	}
	public void setPowerup(Powerup power) 
	{
		powerup = power;
	}
	public Powerup getpowerup() 
	{
		return powerup;
	}
	@Override
	public void onHit(Ball ball, int ID, ArrayList<Brick> bricks,ArrayList<Integer> direc) 
	{
		//Erzeugung eines TwoHitBricks an der Position
		TwoHitBrick block = new TwoHitBrick((int)getShape().getX(),(int) getShape().getY(),(int) getShape().getWidth(),(int) getShape().getHeight());
		//WICHTIG: Powerup weitergeben
		block.setPowerup(powerup);
		bricks.set(ID,block);
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
		return "3HIT";
	}
}

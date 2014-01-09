package bricks;

import java.util.ArrayList;

import main.Ball;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import powerups.Powerup;

//Klasse f�r den 2HIT Brick
//Genau gleich wie beim 1HIT Brick, au�er die onHit Methode
public class TwoHitBrick extends Brick{
	Rectangle brick;
	Powerup powerup;
	
	public TwoHitBrick(int x,int y,int width,int height)
	{
		brick = new Rectangle(x, y, width, height);
	}
	public Shape getShape() 
	{
		return brick;
	}
	public Color getColor()
	{
		return Color.orange;
	}
	public void onHit()
	{
		return;
	}
	public void setPowerup(Powerup power) 
	{
		powerup = power;
	}
	public Powerup getpowerup() 
	{
		return powerup;
	}
	public void setColor(Color color) 
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onHit(Ball ball, int ID, ArrayList<Brick> bricks,ArrayList<Integer> direc) 
	{
		//Erzeugung eines OneHitBricks an der Position
		OneHitBrick block = new OneHitBrick((int)getShape().getX(),(int) getShape().getY(),(int) getShape().getWidth(),(int) getShape().getHeight());
		//WICHTIG: Powerup weitergeben, da Powerups nur beim 1HIT Brick ausgel�st werden sollen
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
		return "2HIT";
	}
}

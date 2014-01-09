package bricks;

import java.util.ArrayList;

import main.Ball;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import powerups.Powerup;

//Repräsentiert einen leeren & transparenten Brick
public class EmptyBrick extends Brick
{
	Rectangle brick;
	Powerup powerup;
	
	public EmptyBrick(int x,int y,int width,int height)
	{
		brick = new Rectangle(x, y, width, height);
	}
	public Shape getShape() 
	{
		return brick;
	}
	public Color getColor()
	{
		//Farbe ist hier unwichtig, der letzte Parameter ist der Alpha-Wert (255 = Voll sichtbar, 0 = transparent)
		return new Color(0, 0, 0, 0);
	}

	//Powerup setzen unnsinig, da Powerups nur bei 1HIT Bricks droppen
	public void setPowerup(Powerup power) 
	{
		powerup = power;
	}
	public Powerup getpowerup() {
		// TODO Auto-generated method stub
		return powerup;
	}
	@Override
	public void onHit(Ball ball, int ID, ArrayList<Brick> bricks,ArrayList<Integer> direc) 
	{
		//Ball geht natürlich durch
	}
	@Override
	public String getName()
	{
		//NONE = Kein richtiger Brick
		return "NONE";
	}
}

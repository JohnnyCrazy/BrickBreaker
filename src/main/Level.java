package main;

import java.util.ArrayList;

import powerups.Powerup;
import bricks.Brick;

//Speichert die Blöcke und das Powerup in sich
public class Level {
	
	ArrayList<Brick> bricks;
	String name;
	
	ParseError error;
	
	public Level(String name)
	{
		this.name = name;
		
		error = ParseError.NONE;
	}
	//Weist einem brick ein Powerup zu
	public void setupBricks(ArrayList<Brick> bricks,ArrayList<Powerup> powerups)
	{
		this.bricks = bricks;
		for(int i = 0;i < bricks.size();i++)
		{
			bricks.get(i).setPowerup(powerups.get(i));
		}
	}
	public ParseError getError()
	{
		return error;
	}
	public void setError(ParseError error)
	{
		this.error = error;
	}
	public ArrayList<Brick> getBricks()
	{
		return bricks;
	}
	public String getName()
	{
		return name;
	}
	//Setzt die Positionen der Bricks
	public void setupPositions() 
	{
		if(bricks.isEmpty())
		{
			return;
		}
		float x = Brick.BRICK_START_X;
		float y = Brick.BRICK_START_Y;
		int count = 1;
		for(Brick block : bricks)
		{
			block.getShape().setX(x);
			block.getShape().setY(y);
			count++;
			x += Brick.BRICK_DISTANCE_WIDTH;
			if(count % 10 == 1)
			{
				y += Brick.BRICK_DISTANCE_HEIGHT;
				x = Brick.BRICK_START_X;
			}
		}
	}
}

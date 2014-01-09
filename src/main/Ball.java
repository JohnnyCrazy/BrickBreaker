package main;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

//Klasse, die den Ball repräsentiert
public class Ball {
	
	//Ball shape
	Circle ball;
	
	//In welche Richtung fliegt der Ball?
	//vector(1F,1F) = Rechts unten da x und y steigen
	Vector2f direction;
	
	//Speed des Balles, abhängig von der
	float speed;
	
	
	public Ball(float x,float y,float radius)
	{
		ball = new Circle(x, y, radius);
		ball.setCenterX(x);
		ball.setCenterY(y);
		direction = new Vector2f(1F,1F);
		speed = 1.3F;
	}
	public Shape getShape()
	{
		return ball;
	}
	public void setX(int x)
	{
		ball.setX(x);
	}
	//Hier wird die Geschwindigkeit gesetzt
	public void setDifficultySpeed(int difficulty)
	{
		switch(difficulty)
		{
		//einfach
		case 0:
			setSpeed(1.3F);
			break;
		//normal
		case 1:
			setSpeed(1.5F);
			break;
		//hard
		case 2:
			setSpeed(1.7F);
			break;
		//eXtrem
		case 3:
			setSpeed(1.9F);
			break;
		}
	}
	
	//Simple getter und setter
	
	public void setY(int y)
	{
		ball.setY(y);
	}
	public float getX()
	{
		return ball.getX();
	}
	public float getY()
	{
		return ball.getY();
	}
	public float getRadius()
	{
		return ball.getRadius();
	}
	public void setRadius(float radius)
	{
		ball.setRadius(radius);
	}
	public Color getColor()
	{
		return Color.white;
	}
	//vector wird aus 2 floats gesetzt
	public void setVec(float x,float y)
	{
		this.direction.x = x;
		this.direction.y = y;
	}
	public Vector2f getVec()
	{
		return direction;
	}
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}
	public float getSpeed()
	{
		return speed;
	}
}

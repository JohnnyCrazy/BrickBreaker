package main;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Player {
	
	
	//Spieler-Shape
	Rectangle paddel;	
	Color col;
	//Leben
	int lives;
	
	//Keys um den Player zu bewegen;
	int left;
	int right;
	//Ist die Steuerung invertiert?
	boolean switched;
	
	//Statische Konstanten bzgl Höhe/Breite
	public static int WIDTH = 80;
	public static int HEIGT = 10;
	
	
	public Player(int x,int y,int width,int height,Color color)
	{		
		paddel = new Rectangle(x, y, width, height);
		col = color;
		lives = 3;
		
		left = Keyboard.KEY_LEFT;
		right = Keyboard.KEY_RIGHT;
	}
	public Rectangle getShape()
	{
		return paddel;
	}
	public Color getColor()
	{
		return col;
	}
	//Invertiere die Steuerung
	public void setSwitched(boolean switched)
	{
		this.switched = switched;
		if(switched)
		{
			setKeys(Keyboard.KEY_RIGHT, Keyboard.KEY_LEFT);
		}
		else
		{
			setKeys(Keyboard.KEY_LEFT, Keyboard.KEY_RIGHT);
		}
		Mouse.setCursorPosition(1000 - Mouse.getX(), Mouse.getY());
	}
	public boolean isSwitched()
	{
		return switched;
	}
	public void setColor(Color col)
	{
		this.col = col;
	}
	//Prüft ob eine Shape die obere Seite des Paddles berührt
	public boolean intersectsOnTop(Shape shape) 
	{
		if(shape.intersects(new Line(paddel.getX(), paddel.getY(), paddel.getX() + paddel.getWidth(),paddel.getY())))
			return true;
		return false;
	}
	public int getLives()
	{
		return lives;
	}
	public void setLives(int lives)
	{
		this.lives = lives;
	}
	//Setzt die Steuerung Keys
	public void setKeys(int left,int right)
	{
		this.left = left;
		this.right = right;
	}
	public int getLeftKey()
	{
		return left;
	}
	public int getRightKey()
	{
		return right;
	}
}

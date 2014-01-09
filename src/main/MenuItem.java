package main;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

//Einfach Getter und Setter, wird in den Menüs verwendet
public class MenuItem {

	String name;
	Vector2f vec;
	Color color;
	
	public MenuItem(String name)
	{
		this.name = name;
		color = Color.white;
	}
	public MenuItem(String name,Color color)
	{
		this.name = name;
		this.color = color;
	}
	public String getName()
	{
		return name;
	}
	public void SetName(String name)
	{
		this.name = name;
	}
	public void setPos(Vector2f vec)
	{
		this.vec = vec;
	}
	public Vector2f getPos()
	{
		return vec;
	}
	public void setColor(Color color)
	{
		this.color = color;
	}
	public Color getColor()
	{
		return color;
	}
}

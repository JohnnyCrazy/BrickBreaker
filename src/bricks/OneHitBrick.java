package bricks;
import java.util.ArrayList;

import main.Ball;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import powerups.Powerup;

//Klasse für den 1HIT Brick
public class OneHitBrick extends Brick{
	Rectangle brick;
	Powerup powerup;
	
	public OneHitBrick(int x,int y,int width,int height)
	{
		//Erstelle ein neues Rechteck
		brick = new Rectangle(x, y, width, height);
	}
	public Shape getShape() 
	{
		return brick;
	}
	public String getName()
	{
		//1HIT = Mit einem Ball-Hit ist der brick zerstört
		return "1HIT";
	}
	public Color getColor()
	{
		//green bedeuted 1HIT
		return Color.green;
	}
	
	//Speicherung des Powerups
	@Override
	public void setPowerup(Powerup powerup) 
	{
		this.powerup = powerup;
	}
	@Override
	public Powerup getpowerup()
	{
		return powerup;
	}
	
	//Was soll passieren wenn der Ball auftrifft?
	@Override
	public void onHit(Ball ball, int ID, ArrayList<Brick> bricks,ArrayList<Integer> direc) 
	{
		//An der Position, wo vorher dieser Brick war, soll nun ein EmptyBrick erstellt werden ( Logischerweiße gleiche Koordinaten)
		bricks.set(ID, new EmptyBrick((int)getShape().getX(),(int) getShape().getY(),(int) getShape().getWidth(),(int) getShape().getHeight()));
		
		//direc.size gibt die Anzahl an getroffenen Seiten an, wenn eine Ecke getroffen wurde, müsste direc.size == 2 sein
		if(direc.size() == 1)
		{
			switch(direc.get(0))
			{
			//Ball kommt aus folgender Richtung
			//North
			case 0:
				ball.setVec(ball.getVec().x, -ball.getVec().y);
				break;
			//South
			case 1:
				ball.setVec(ball.getVec().x, -ball.getVec().y);
				break;
			//East
			case 2:
				ball.setVec(-ball.getVec().x,ball.getVec().y);
				break;
			//West
			case 3:
				ball.setVec(-ball.getVec().x,ball.getVec().y);
				break;
			}
		}
		//Ball trifft auf eine Ecke -> Ball wird 180° zurück gelenkt
		else if(direc.size() == 2)
		{
			
			ball.setVec(-ball.getVec().x,-ball.getVec().y);
		}
		//Falls das unmögliche passiert(Keine der 2 Fälle trifft ein), wird eine Exception ausgelöst
		else
		{
			new Exception("Error in collision detection");
		}
	}
}

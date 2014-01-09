package bricks;

import java.util.ArrayList;

import main.Ball;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Shape;

import powerups.Powerup;

//Abstrakte Klasse für alle Bricks bzw. Blöcke

public abstract class Brick {
	
	//statische Konstanten, welche vom Spiel aus nicht verändert werden
	//Für die Levelerstellung wichtig, siehe Klasse "Level" -> setupPositions()
	public static int BRICK_WIDHT = 80;
	public static int BRICK_HEIGHT = 20;
	public static float BRICK_DISTANCE_WIDTH = BRICK_WIDHT + 1F;
	public static float BRICK_DISTANCE_HEIGHT = BRICK_HEIGHT + 1F;
	public static float BRICK_START_X = 96F;
	public static float BRICK_START_Y = 100;
	
	//getter und setter
	public abstract Shape getShape();
	public abstract Color getColor();
	public abstract String getName();
	
	//einem brick wird ein Powerup zugewiesen.
	public abstract void setPowerup(Powerup power);
	public abstract Powerup getpowerup();
	
	//Diese Methode wird aufgerufen, sobald der Ball diesen Brick berührt
	//(Unterschiedliche Handhabung abhängig vom Brick-Type)
	public abstract void onHit(Ball ball, int ID,ArrayList<Brick> bricks,ArrayList<Integer> direc);
}

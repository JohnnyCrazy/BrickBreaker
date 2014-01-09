package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import powerups.BallGrow;
import powerups.BallShrink;
import powerups.Bomb;
import powerups.None;
import powerups.PlayerGrow;
import powerups.PlayerShrink;
import powerups.PlayerSwitch;
import powerups.Powerup;
import bricks.Brick;
import bricks.EmptyBrick;
import bricks.OneHitBrick;
import bricks.SolidBrick;
import bricks.ThreeHitBrick;
import bricks.TwoHitBrick;

//LevelManager verwaltet das Laden und Speichern der Levels
public class LevelManager {
	
	//Wieder eine statische Instanz => Zugriff von Überall
	private static LevelManager instance = new LevelManager();
	
	public LevelManager()
	{
		//Nachschauen, ob die levels Directory existiert
		checkLevelDirec();
		System.out.println("test");
	}
	public static LevelManager getInstance()
	{
		return instance;
	}
	private boolean checkName(File f)
	{
		//Check, ob die Level Datei legitim ist
		if(f.getName().endsWith(".txt") || !f.exists())
			return true;
		return false;
	}
	//Speichert ein Level in einer .txt File
	public void saveLevel(Level lvl)
	{
		File f = new File("./levels/" + lvl.getName() + ".txt");
		//Falls die Datei existiert, lösche die alte Datei
		if(f.exists())
			f.delete();
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f, false));
			
			ArrayList<Brick> bricks = lvl.getBricks();
			
			//Jeder Brick wird rein geschrieben
			for(int i = 0;i < bricks.size();i++)
			{
				bw.write(bricks.get(i).getName() + ":" + bricks.get(i).getpowerup().getName());
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//Erstelle ein neues Level, und fülle es mit OneHit Bricks und keinen Powerups
	public Level createLevel(String name)
	{
		Level lvl = new Level(name);
		ArrayList<Brick> bricks = new ArrayList<Brick>();
		ArrayList<Powerup> powerups = new ArrayList<Powerup>();
		for(int i = 0;i < 90;i++)
		{
			bricks.add(new OneHitBrick(0, 0, Brick.BRICK_WIDHT, Brick.BRICK_HEIGHT));
			powerups.add(new None());
		}
		lvl.setupBricks(bricks,powerups);
		return lvl;
	}
	//Lädt ein Level aus einer Datei
	public Level getLevel(String name)
	{
		File f = new File("./levels/" + name + ".txt");
		if(!checkName(f))
			return null;
		Level lvl = new Level(f.getName().replace(".txt", ""));
		ArrayList<String> all = new ArrayList<String>();
		BufferedReader reader = null;
		try 
		{
			reader = new BufferedReader(new FileReader(f));
			String row = reader.readLine();
			
			while(row != null)
			{
				all.add(row);
				row = reader.readLine();
			}
			reader.close();
		}
		catch (IOException e) 
		{
			return null;
		}
		ArrayList<Brick> blocks = new ArrayList<Brick>();
		ArrayList<Powerup> powerups = new ArrayList<Powerup>();
		
		//ParseError wird gesetzt, falls z.B nur 89 Einträge in der Text Datei sind
		if(all.size() != 90)
		{
			if(all.size() > 90)
				lvl.setError(ParseError.TOO_MANY_BRICKS);
			else
				lvl.setError(ParseError.INSUFFICIENT_BRICKS);
		}
		for(String type : all)
		{
			Brick block = getBlockForName(type.split(":")[0]);
			//Falls der Block nicht existiert --> ParseError
			if(block == null)
				lvl.setError(ParseError.UNKOWN_BRICK);
			blocks.add(block);
			
			Powerup powerup = getPowerupForName(type.split(":")[1]);
			//Falls das Powerup nicht existiert --> ParseError
			if(powerup == null)
				lvl.setError(ParseError.UNKOWN_POWERUP);
			powerups.add(powerup);
		}
		lvl.setupBricks(blocks,powerups);
		return lvl;
	}
	private void checkLevelDirec()
	{
		//Existiert die levels Directory?
		File directory = new File("./levels/");
		if(!directory.exists())
		{
			try {
				Files.createDirectory(directory.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	public ArrayList<String> getLevelList()
	{
		ArrayList<String> levels = new ArrayList<>();
		File directory = new File("./levels/");
		for(File level : directory.listFiles())
		{
			if(level.getName().endsWith(".txt"))
				levels.add(level.getName().replace(".txt", ""));
		}
		return levels;
	}
	//String wird zu einem Brick umgewandelt 
	public Brick getBlockForName(String name)
	{
		switch(name)
		{
		case "NONE":
			return new EmptyBrick(0, 0, Brick.BRICK_WIDHT, Brick.BRICK_HEIGHT);
		case "1HIT":
			return new OneHitBrick(0, 0,Brick.BRICK_WIDHT, Brick.BRICK_HEIGHT);
		case "2HIT":
			return new TwoHitBrick(0, 0, Brick.BRICK_WIDHT, Brick.BRICK_HEIGHT);
		case "3HIT":
			return new ThreeHitBrick(0, 0, Brick.BRICK_WIDHT, Brick.BRICK_HEIGHT);
		case "SOLID":
			return new SolidBrick(0, 0, Brick.BRICK_WIDHT, Brick.BRICK_HEIGHT);
			default:
				return null;
		}
	}
	//String wird zu einem Powerup umgewandelt 
	public Powerup getPowerupForName(String name)
	{
		switch(name)
		{
		case "NONE":
			return new None();
		case "BallGrow":
			return new BallGrow();
		case "BallShrink":
			return new BallShrink();
		case "PlayerShrink":
			return new PlayerShrink();
		case "PlayerGrow":
			return new PlayerGrow();
		case "PlayerSwitch":
			return new PlayerSwitch();
		case "Bomb":
			return new Bomb();
			default:
				return null;
		}
	}
}

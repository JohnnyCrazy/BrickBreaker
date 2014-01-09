package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.newdawn.slick.Color;

public class SettingsManager {
	
	private static SettingsManager instance = new SettingsManager();
	
	//0 = keyboard, 1 = Mouse
	public int controls;
	//0 = easy 1 = normal 2 = hard 3 = EXTREME
	public int difficulty;
	//verschieden
	public Color color;
	//Powerups enabled?
	public boolean powerups;
	//Fps enabled?
	public boolean FPS;	
	
	public static SettingsManager getInstance()
	{
		return SettingsManager.instance;
	}
	//Speicherung der Settings
	public boolean save()
	{
		File f = new File("./settings.ini");
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write("##SETTINGS FILE##");
			bw.newLine();
			bw.write("CONTROLS=" + controls);
			bw.newLine();
			bw.write("DIFFICULTY=" + difficulty);
			bw.newLine();
			bw.write("COLOR=" + color.r + ":" + color.g + ":" + color.b);
			bw.newLine();
			int temp_powerup = powerups? 1 : 0;
			bw.write("POWERUPS=" + temp_powerup);
			bw.newLine();
			int temp_FPS = FPS? 1 : 0;
			bw.write("SHOWFPS=" + temp_FPS);
			bw.newLine();
			bw.write("##End##");
			bw.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public void createSettingsFile(File f) 
	{
		JOptionPane.showMessageDialog(null, "settings.ini wurde nicht gefunden und wurde neu erstellt", "Settings neu erstellt", JOptionPane.OK_OPTION | JOptionPane.INFORMATION_MESSAGE);
		try {
			f.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			
			bw.write("##WSETTINGS FILE##");
			bw.newLine();
			bw.write("CONTROLS=0");
			bw.newLine();
			bw.write("DIFFICULTY=1");
			bw.newLine();
			bw.write("COLOR=" + Color.green.r + ":" + Color.green.g + ":" + Color.green.b);
			bw.newLine();
			bw.write("POWERUPS=1");
			bw.newLine();
			bw.write("SHOWFPS=1");
			bw.newLine();
			bw.write("##End##");
			bw.flush();
			bw.close();
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//Auslesen der Settings
	public boolean readSettings()
	{
		File f = new File("./settings.ini");
		if(!f.exists())
		{
			createSettingsFile(f);
		}
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(f));
			String row = "";
			while((row = br.readLine()) != null)
			{
				// # = Kommentar
				if(row.startsWith("#"))
					continue;
				else if(row.startsWith("CONTROLS"))
					controls = Integer.parseInt(row.split("=")[1]);
				else if(row.startsWith("DIFFICULTY"))
					difficulty = Integer.parseInt(row.split("=")[1]);
				else if(row.startsWith("COLOR"))
				{
					String[] rgb = row.split("=")[1].split(":");
					color = new Color(Float.parseFloat(rgb[0]),Float.parseFloat(rgb[1]),Float.parseFloat(rgb[2]));
				}
				else if(row.startsWith("POWERUPS"))
					powerups = getBoolOfString(row.split("=")[1]);
				else if(row.startsWith("SHOWFPS"))
					FPS = getBoolOfString(row.split("=")[1]);
					
			}
			br.close();
		}catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public String getDifficultyAsString()
	{
		switch(difficulty)
		{
		case 0:
			return "einfach";
		case 1:
			return "normal";
		case 2:
			return "hard";
		case 3:
			return "eXtrem";
		default:
			return "-";
		}
	}
	public Color getDifficultyColor()
	{
		switch(difficulty)
		{
		case 0:
			return Color.pink;
		case 1:
			return Color.green;
		case 2:
			return Color.red;
		case 3:
			return Color.black;
		default:
			return Color.white;
		}
	}
	public int getDifficulty()
	{
		return difficulty;
	}
	public boolean powerupsEnabled()
	{
		return powerups;
	}
	public int getControls()
	{
		return controls;
	}
	public boolean showFPS()
	{
		return FPS;
	}
	public boolean arePowerupsEnabled()
	{
		return powerups;
	}
	public Color getPlayerColor()
	{
		return color;
	}
	private boolean getBoolOfString(String i)
	{
		if(i.equalsIgnoreCase("1"))
			return true;
		else if(i.equalsIgnoreCase("0"))
			return false;
		throw new Error("Error converting int to Boolean, check settings.ini");
	}
	
	//Damit man auch ohne Objekt umwandeln kann
	public static String getDifficultyAsString(int difficulty)
	{
		switch(difficulty)
		{
		case 0:
			return "einfach";
		case 1:
			return "normal";
		case 2:
			return "hard";
		case 3:
			return "eXtrem";
		default:
			return "-";
		}
	}
	//Damit man auch ohne Objekt umwandeln kann
	public static Color getDifficultyColor(int difficulty)
	{
		switch(difficulty)
		{
		case 0:
			return Color.pink;
		case 1:
			return Color.green;
		case 2:
			return Color.red;
		case 3:
			return Color.darkGray;
		default:
			return Color.white;
		}
	}
	public void setPlayerColor(Color color)
	{
		System.out.println(color);
		this.color = color;
	}
	public void setControls(int controls)
	{
		this.controls = controls;
	}
	public void setPowerupsEnabled(boolean powerups)
	{
		this.powerups = powerups;
	}
	public void setDifficulty(int difficulty)
	{
		this.difficulty = difficulty;
	}
	public void setFPSEnabled(boolean FPS)
	{
		this.FPS = FPS;
	}
}

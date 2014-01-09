package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

//Behandelt alle Highscore spezifischen Sachen
public class HighscoreManager {
	
	//Statische Variable behinhaltet die Instanz --> Zugriff von überall
	private static HighscoreManager instance = new HighscoreManager();
	
	//Ausgelesene Highscores speichern
	ArrayList<Highscore> highscores;
	
	public HighscoreManager()
	{
		highscores = new ArrayList<Highscore>();
	}	
	public static HighscoreManager getInstance()
	{
		return instance;
	}
	
	public void readHighscores()
	{
		//Lese Datei "highscores"
		File f = new File("./highscores");
		if(!f.exists())
			//Wenn nicht vorhanden -> Erstelle neue
			createHighscoreFile(f);		
		//Lösche alte Highscores
		highscores.clear();
		try {
			//Neu Laden der Datei, falls sie erstellt wurde
			f = new File("./highscores");
			BufferedReader br = new BufferedReader(new FileReader(f));
			String row = "";
			//Wenn die Zeile nicht null ist
			while((row = br.readLine()) != null)
			{
				//Kommentare starten mit #
				if(row.startsWith("#"))
					continue;
				String[] splitted = row.split(":");
				
				
				Highscore hs = new Highscore(splitted[0],splitted[1],Long.parseLong(splitted[2]));
				//Loop durch alle bisher eingetragenen Highscores
				for (Iterator<Highscore> it = highscores.iterator(); it.hasNext(); )
				{
					Highscore s = it.next();
					//Falls der eingetragene Highscore(s) schlechter ist, und das Level gleich heißt, entferne das eingetragene
					if(s.getTime() >= hs.getTime() && s.getLevelName().equalsIgnoreCase(hs.getLevelName()))
					{
						it.remove();
					}
				}
				
				highscores.add(hs);
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//Layout der Datei:
	//LEVELNAME:SPIELER:ZEIT
	public void createHighscoreFile(File f) 
	{
		try {
			
			f.createNewFile();
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("#HIGHSCORES FILE#");
			bw.flush();
			bw.close();
			fw.close();
			Runtime.getRuntime().exec("attrib +H " + f.getName());
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void addNewHighscore(Highscore score)
	{
		try {
			//Erstellung, da nicht vorhanden
			File f = new File("./highscores");
			FileWriter fw = new FileWriter(f,true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.newLine();
			bw.write(score.getLevelName() + ":" + score.getPlayerName() + ":" + score.getTime());
			bw.close();
			fw.close();
			
			//Die Datei nicht sichtbar machen, Cheat-Schutz :)
			Runtime.getRuntime().exec("attrib +H " + f.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<Highscore> getHighscores()
	{
		return highscores;
	}
	//Besteht schon eine Highscore mit diesm Levelnamen?
	public boolean isDuplicateLevel(String levelname)
	{
		for(Highscore hs : highscores)
		{
			if(hs.getLevelName().equalsIgnoreCase(levelname))
				return true;
		}
		return false;
	}
	//Prüft, ob der angebene Highscore besser ist, als die, die in der ArrayList sind (Muss gleichen LevelNamen haben!)
	public boolean isBetter(Highscore score)
	{
		for(Highscore hs : highscores)
		{
			if(hs.getLevelName().equalsIgnoreCase(score.getLevelName()))
				if(hs.getTime() < score.getTime())
					return false;
		}
		return true;
	}
}
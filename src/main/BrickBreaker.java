package main;

import gamestates.HighscoreMenu;
import gamestates.Ingame;
import gamestates.Instructions;
import gamestates.LevelSelection;
import gamestates.MainMenu;
import gamestates.SettingsMenu;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

//HauptKlasse, hier wird alles gestartet und initialisiert
public class BrickBreaker extends StateBasedGame {

	public BrickBreaker() {
		//Namen des Fensters an die Superklasse übergeben
		super("BrickBreaker - 1.0 RELEASE");
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException 
	{
		//Laden des Backgrounds
		Image background = new Image("./resources/background.jpg");

		//Sogentannte States müssen definiert werden
		//Danach kann man mit sb.enterState(ID) die Klasse wechseln
		//Mit sb.enterState(5) würde die Anleitung angezeigt werden
		this.addState(new MainMenu(0, background));
		this.addState(new LevelSelection(1, background));
		this.addState(new Ingame(2, background,gc));
		this.addState(new HighscoreMenu(3, background));
		this.addState(new SettingsMenu(4, background));
		this.addState(new Instructions(5, background));
	}

	public static void main(String[] args) throws SlickException 
	{
		//Erstellung des Programms/Fensters
		AppGameContainer app = new AppGameContainer(new BrickBreaker());
		app.setShowFPS(false);
		app.setDisplayMode(1000, 700, false);
		// app.setVSync(true); Nur anmachen, falls es zu problemen kommt
		
		//Auf gehts...
		app.start();
	}

}

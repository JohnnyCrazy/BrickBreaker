package gamestates;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import main.MenuItem;
import main.SettingsManager;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

//Ähnliche Klasse wie bei HighscoreMenu,LevelSelection
public class SettingsMenu extends BasicGameState
{
	int id;
	Image background;
	boolean showFPS;
	
	//Fonts
	Font font;
	UnicodeFont uniTitle;
	UnicodeFont uniNormal;
	
	//Wurde speichern gedrückt?
	boolean saved;
	
	//Welche Reihe ist ausgewählt
	int currentIndex;
	int count;
	
	Rectangle player;
	Rectangle selection;
	Polygon polyOne;
	Polygon polyTwo;
	
	ArrayList<MenuItem> leftKeys;
	ArrayList<MenuItem> rightValues;
	ArrayList<Color> colors;
	MenuItem saveString;
	
	
	int currentColor;
	int controls;
	int difficulty;
	boolean powerups;
	boolean FPS;
	
	public SettingsMenu(int id,Image background)
	{
		this.id = id;
		this.background = background;
	}
	
	
	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException 
	{
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("./resources/pixel.ttf"));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		uniTitle = new UnicodeFont(font, 100, true, false);
		uniTitle.getEffects().add(new ColorEffect(java.awt.Color.orange));
		uniTitle.addAsciiGlyphs();
		uniTitle.loadGlyphs();
		
		uniNormal = new UnicodeFont(font, 30, true, false);
		uniNormal.getEffects().add(new ColorEffect(java.awt.Color.white));
		uniNormal.addAsciiGlyphs();
		uniNormal.loadGlyphs();
		
		player = new Rectangle(0, 0, 200, 200);
		selection = new Rectangle(0, 0, 0, 40);
		
		saveString = new MenuItem("Speichern",Color.orange);
		saveString.setPos(new Vector2f(gc.getWidth()/2 - uniNormal.getWidth(saveString.getName())/2, gc.getHeight()/3*2));
		
		//Links = Keys
		leftKeys = new ArrayList<MenuItem>();
		//Rechts = Values
		rightValues = new ArrayList<MenuItem>();
		
		//Farben-Liste für die Spieler-Farben
		colors = new ArrayList<Color>();
		
		colors.add(Color.red);
		colors.add(Color.green);
		colors.add(Color.cyan);
		colors.add(Color.darkGray);
		colors.add(Color.magenta);
		colors.add(Color.orange);
		colors.add(Color.pink);
		colors.add(Color.white);
		colors.add(Color.yellow);
		colors.add(Color.blue);
		colors.add(Color.gray);
		
		leftKeys.add(new MenuItem("Steuerung"));
		leftKeys.add(new MenuItem("Schwierigkeit"));
		leftKeys.add(new MenuItem("Spieler-Farbe"));
		leftKeys.add(new MenuItem("Powerups"));
		leftKeys.add(new MenuItem("FPS anzeigen?"));
		
		//Positionen mal wieder
		int y = 200;
		for(MenuItem item : leftKeys)
		{
			item.setPos(new Vector2f(gc.getWidth()/3 - uniNormal.getWidth(item.getName())/2,y));
			y += 50;
		}
		
		//Gleich wie im MainMenu
		float[] polyonecoords = new float[6];
		polyonecoords[0] = 400F;
		polyonecoords[1] = 400F;
		polyonecoords[2] = 400F;
		polyonecoords[3] = 420F;
		polyonecoords[4] = 425F;
		polyonecoords[5] = 410F;
		polyOne = new Polygon(polyonecoords);
		
		float[] polyTwoCoords = new float[6];
		polyTwoCoords[0] = 400F;
		polyTwoCoords[1] = 400F;
		polyTwoCoords[2] = 400F;
		polyTwoCoords[3] = 420F;
		polyTwoCoords[4] = 375F;
		polyTwoCoords[5] = 410F;
		polyTwo = new Polygon(polyTwoCoords);
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sb)
	{
		//Aktualisiere die Values ( Wird vom Settingsmanager übergeben )
		
		
		rightValues.clear();
		saved = false;
		SettingsManager instance = SettingsManager.getInstance();
		
		controls = instance.getControls();
		switch(controls)
		{
		case 0:
			rightValues.add(new MenuItem("Tastatur",Color.green));
			break;
		case 1:
			rightValues.add(new MenuItem("Maus",Color.green));
			break;
		}
		difficulty = instance.getDifficulty();
		switch(difficulty)
		{
		case 0:
			rightValues.add(new MenuItem("einfach",Color.pink));
			break;
		case 1:
			rightValues.add(new MenuItem("normal",Color.green));
			break;
		case 2:
			rightValues.add(new MenuItem("hart",Color.red));
			break;
		case 3:
			rightValues.add(new MenuItem("eXtrem",Color.darkGray));
			break;
		}	
		rightValues.add(new MenuItem("Player",Color.black));
		count = 0;
		for(Color col : colors)
		{
			if(col.toString().equalsIgnoreCase(instance.getPlayerColor().toString()))
				currentColor = count;
			count++;
		}
		
		powerups = instance.arePowerupsEnabled();
		if(powerups)
			rightValues.add(new MenuItem("An",Color.green));
		else
			rightValues.add(new MenuItem("Aus",Color.red));
		
		FPS = instance.showFPS();
		if(FPS)
			rightValues.add(new MenuItem("An",Color.green));
		else
			rightValues.add(new MenuItem("Aus",Color.red));
	
		
		//Positionen sethen
		int y = 200;
		for(MenuItem item : rightValues)
		{
			item.setPos(new Vector2f(gc.getWidth()/3*2 - uniNormal.getWidth(item.getName())/2,y));
			if(item.getName().equalsIgnoreCase("Player"))
			{
				player.setBounds(item.getPos().x - 5, item.getPos().y, uniNormal.getWidth(item.getName()) + 10, 30);
			}
			y += 50;
		}
		
		showFPS = this.FPS;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException 
	{
		g.setAntiAlias(true);
		
		//Background zeichnen, mit Berechnungen damit es mittig ist
		g.drawImage(background, gc.getWidth()/2 - background.getWidth()/2, gc.getHeight()/2 - background.getHeight()/2);
		
		//Falls speichern gedrückt wurde --> String anzeigen
		if(saved)
			uniNormal.drawString(gc.getWidth()/2 - uniNormal.getWidth("Speichern erfolgreich")/2,gc.getHeight() - uniNormal.getHeight("Speichern erfolgreich") - 50,"Speichern erfolgreich",Color.orange);
		
		uniTitle.drawString(gc.getWidth()/2 - uniTitle.getWidth("Optionen")/2 ,(float) 0, "Optionen",Color.orange);
		uniNormal.drawString(0, gc.getHeight() - uniNormal.getHeight("ESC = Zurück"), "ESC = Zurück", Color.orange);
		
		g.setColor(colors.get(currentColor));
		g.fill(player);
		
		for(MenuItem item : leftKeys)
		{
			uniNormal.drawString(item.getPos().x, item.getPos().y, item.getName(),item.getColor());
		}
		for(MenuItem item : rightValues)
		{
			uniNormal.drawString(item.getPos().x, item.getPos().y, item.getName(),item.getColor());
		}
		uniNormal.drawString(saveString.getPos().x,saveString.getPos().y, saveString.getName(),Color.orange);
		
		
		g.setColor(Color.green);
		g.draw(selection);
		if(currentIndex != leftKeys.size())
		{
			g.fill(polyOne);
			g.fill(polyTwo);
		}
		
		if(showFPS)
		{
			g.setColor(Color.green);
			g.drawString("FPS " + gc.getFPS(), gc.getWidth() - 100, gc.getHeight() - 40);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException 
	{
		background.rotate(0.05F * delta);
		Input input = gc.getInput();
		if(input.isKeyPressed(Input.KEY_ESCAPE))
			sb.enterState(0);		
		if(input.isKeyPressed(Input.KEY_ENTER) || input.isKeyPressed(Input.KEY_SPACE))
		{
			if(currentIndex == leftKeys.size())
			{
				//Speichern wurde gedrückt
				save();
			}
		}
		if(input.isKeyPressed(Input.KEY_DOWN))
			if(currentIndex + 1 <= leftKeys.size())
				currentIndex++;
		if(input.isKeyPressed(Input.KEY_UP))
			if(currentIndex - 1 >= 0)
				currentIndex--;
		if(input.isKeyPressed(Input.KEY_RIGHT))
		{
			//Ausgewählte Setting nach rechts
			changeSettings(0);
		}
		if(input.isKeyPressed(Input.KEY_LEFT))
		{
			//Ausgewählte Setting nach links
			changeSettings(1);
		}
		
		if(currentIndex == leftKeys.size())
		{
			//Falls wir den Button "Speichern" ausgewählt haben
			selection.setBounds(saveString.getPos().x - 5, saveString.getPos().y - 5, uniNormal.getWidth(saveString.getName()) + 10, 40);
		}
		
		//Update positionen
		count = 0;
		for(MenuItem item : leftKeys)
		{
			item.setPos(new Vector2f(gc.getWidth()/3 - uniNormal.getWidth(item.getName())/2,item.getPos().y));
			if(count == currentIndex)
				selection.setLocation(item.getPos().x - 5, item.getPos().y - 5);
			count++;
		}
		count = 0;
		for(MenuItem item : rightValues)
		{
			item.setPos(new Vector2f(gc.getWidth()/3*2 - uniNormal.getWidth(item.getName())/2,item.getPos().y));
			if(count == currentIndex)
			{
				selection.setWidth(item.getPos().x + uniNormal.getWidth(item.getName()) - selection.getX() + 50);
				polyTwo.setCenterY(item.getPos().y + uniNormal.getHeight(item.getName())/2);
				polyTwo.setCenterX(item.getPos().x - 20);
				
				polyOne.setCenterY(item.getPos().y + uniNormal.getHeight(item.getName())/2);
				polyOne.setCenterX(item.getPos().x + uniNormal.getWidth(item.getName()) + 20);
			}
			count++;
		}
	}
	//0 = Key Right        1 = Key Left
	public void changeSettings(int key)
	{
		switch(currentIndex)
		{
		//Steuerung
		case 0:
			if(controls == 0)
			{
				rightValues.get(0).SetName("Maus");
				controls = 1;
			}
			else
			{
				rightValues.get(0).SetName("Tastatur");
				controls = 0;
			}
			break;
		//Schwierigkeit
		case 1:
			if(key == 0)
			{
				if(difficulty + 1 <= 3)
				{
					difficulty++;
					rightValues.get(1).SetName(SettingsManager.getDifficultyAsString(difficulty));
					rightValues.get(1).setColor(SettingsManager.getDifficultyColor(difficulty));
				}
			}
			if(key == 1)
			{
				if(difficulty - 1 >= 0)
				{
					difficulty--;
					rightValues.get(1).SetName(SettingsManager.getDifficultyAsString(difficulty));
					rightValues.get(1).setColor(SettingsManager.getDifficultyColor(difficulty));
				}
			}
			break;
		//Spieler-Farbe
		case 2:
			if(key == 0)
			{
				if(currentColor + 1 < colors.size())
					currentColor++;
				else
					currentColor = 0;
			}
			if(key == 1)
			{
				if(currentColor - 1 >= 0)
					currentColor--;
				else
					currentColor = colors.size() - 1;
			}
			break;
		//Powerups an?
		case 3:
			if(powerups)
			{
				rightValues.get(3).SetName("Aus");
				rightValues.get(3).setColor(Color.red);
				powerups = false;
			}
			else
			{
				rightValues.get(3).SetName("An");
				rightValues.get(3).setColor(Color.green);
				powerups = true;
			}
			break;
		//FPS anzeigen?
		case 4:
			if(FPS)
			{
				rightValues.get(4).SetName("Aus");
				rightValues.get(4).setColor(Color.red);
				FPS = false;
			}
			else
			{
				rightValues.get(4).SetName("An");
				rightValues.get(4).setColor(Color.green);
				FPS = true;
			}
			showFPS = FPS;
			break;
		}
	}
	public void save()
	{
		//Alles an den Settingsmanager übergeben
		SettingsManager.getInstance().setPlayerColor(colors.get(currentColor));
		SettingsManager.getInstance().setControls(controls);
		SettingsManager.getInstance().setDifficulty(difficulty);
		SettingsManager.getInstance().setFPSEnabled(FPS);
		SettingsManager.getInstance().setPowerupsEnabled(powerups);
		SettingsManager.getInstance().save();
		saved = true;
	}

	@Override
	public int getID() 
	{
		return id;
	}

}

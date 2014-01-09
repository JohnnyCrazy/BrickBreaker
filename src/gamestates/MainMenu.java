package gamestates;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import main.HighscoreManager;
import main.LevelDesigner;
import main.MenuItem;
import main.SettingsManager;

import org.lwjgl.opengl.Display;
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
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

//Ähnlich wie HighScoreMenu bzw LevelSelection
public class MainMenu extends BasicGameState
{
	int id;
	Image image;
	boolean showFPS;
	
	Font font;
	UnicodeFont uni;
	
	
	//Die MenuItems
	ArrayList<MenuItem> menuItems;
	int currentItem = 0;
	int count;
	
	//Für die Dreiecke benutze ich Polygone
	Polygon polyOne;
	Polygon polyTwo;
	
	Image background;
	
	public MainMenu(int id,Image background)
	{
		this.background = background;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		image = new Image("./resources/title.png");
		try {
			font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT,new File("./resources/pixel.ttf"));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		uni = new UnicodeFont(font, 40, true, false);
		uni.getEffects().add(new ColorEffect());
		uni.addAsciiGlyphs();
		uni.loadGlyphs();
	
		
		menuItems = new ArrayList<MenuItem>();
		
		//Items hinzufügen
		menuItems.add(new MenuItem("START"));
		menuItems.add(new MenuItem("HIGHSCORES"));
		menuItems.add(new MenuItem("LEVEL-EDITOR"));
		menuItems.add(new MenuItem("OPTIONEN"));
		menuItems.add(new MenuItem("ANLEITUNG"));
		menuItems.add(new MenuItem("VERLASSEN"));		
		
		//Setup der Positionen
		int y = gc.getHeight()/2 + 50;
		for(MenuItem mi : menuItems)
		{
			mi.setPos(new Vector2f(gc.getWidth()/2 - uni.getWidth(mi.getName())/2,y));
			y += 50;
		}
		
		//Setup der Koordinaten
		//2 gehören immer zusammen: [0] = x [1] = y
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
		
		//Highscores aktualisieren und Settings einlesen
		HighscoreManager.getInstance().readHighscores();
		SettingsManager.getInstance().readSettings();	
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		g.setAntiAlias(true);
		g.drawImage(background, gc.getWidth()/2 - background.getWidth()/2, gc.getHeight()/2 - background.getHeight()/2);
		
		//Zeichne Titelbild
		g.drawImage(image, -50, 0);
		
		//Zeichne alle Items
		for(MenuItem mi : menuItems)
		{	
			uni.drawString(mi.getPos().x, mi.getPos().y,mi.getName(),Color.orange);
		}
		g.setColor(Color.green);
		g.fill(polyOne);
		g.fill(polyTwo);
		g.setColor(Color.gray);
		g.drawString("Erstellt von Jonas Dellinger", 0, gc.getHeight()- 20);
		
		//Falls FPS aktiviert sind, zeichnen
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
		if(input.isKeyPressed(Input.KEY_ENTER) || input.isKeyPressed(Input.KEY_SPACE))
		{
			enterPressed(gc,sb);	
		}
		if(input.isKeyPressed(Input.KEY_DOWN))
		{
			if(currentItem + 1 < menuItems.size())
				currentItem++;
			else 
				currentItem = 0;
		}
		if(input.isKeyPressed(Input.KEY_UP))
		{
			if(currentItem - 1 >= 0)
				currentItem--;
			else
				currentItem = menuItems.size() - 1;
		}		
		count = 0;
		for(MenuItem mi : menuItems)
		{	
			if(currentItem == count)
			{
				polyOne.setCenterY(mi.getPos().y + uni.getHeight(mi.getName())/2);
				polyOne.setCenterX(mi.getPos().x - 20);
				
				polyTwo.setCenterY(mi.getPos().y + uni.getHeight(mi.getName())/2);
				polyTwo.setCenterX(mi.getPos().x + uni.getWidth(mi.getName()) + 20);
			}
			count++;
		}
	}
	@Override
	public void enter(GameContainer gc,StateBasedGame sb)
	{
		showFPS = SettingsManager.getInstance().showFPS();
		gc.setMouseGrabbed(false);
		gc.resume();
	}
	public void enterPressed(GameContainer gc,StateBasedGame sb)
	{
		switch (currentItem) {
		case 0:
			sb.enterState(1);
			break;
		case 1:
			sb.enterState(3);
			break;
		case 2:
			LevelDesigner.start(Display.getX(), Display.getY());
			break;
		case 3:
			sb.enterState(4);
			break;
		case 4:
			sb.enterState(5);
			break;
		case 5:
			gc.exit();
			break;
		}
	}
	@Override
	public int getID() 
	{
		return id;
	}
}
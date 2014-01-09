package gamestates;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import main.Level;
import main.LevelManager;
import main.MenuItem;
import main.ParseError;
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
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

//Kopie der Klasse HighscoreMenu, nur abgeändert
public class LevelSelection extends BasicGameState {

	int id;
	Image background;
	Boolean showFPS;
	
	ArrayList<main.MenuItem> list;
	int currentIndex;
	Rectangle selection;
	int sideCount = 1;
	int side = 0;
	int itemsOnSide = 0;
	
	//Items in der Seite, auf Seite 0: Start=1 End=10;
	int start;
	int end;
	
	
	Font font;
	UnicodeFont uniTitle;
	UnicodeFont uniNormal;
	
	public static Level selectedLevel = null;
	
	
	public LevelSelection(int id, Image background)
	{
		this.id = id;
		this.background = background;
		selection = new Rectangle(50, 50, 0, 40);
		
		list = new ArrayList<MenuItem>();
		
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException 
	{
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("./resources/pixel.ttf"));
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
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
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sb)
	{
		showFPS = SettingsManager.getInstance().showFPS();
		
		list.clear();
		ArrayList<String> levellist = LevelManager.getInstance().getLevelList();
		if(levellist.isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Keine Level vorhanden!", "Keine Level vorhanden!", JOptionPane.INFORMATION_MESSAGE);
			sb.enterState(0);
		}
		String error = "";
		for(String name : levellist)
		{
			Level lvl = LevelManager.getInstance().getLevel(name);
			
			if(lvl.getError() != ParseError.NONE)
			{
				error += (lvl.getName() + "------->" + lvl.getError().toString() + System.getProperty("line.separator"));
			}
			list.add(new MenuItem(lvl.getName(), Color.white));
		}
		if(error != "")
		{
			JOptionPane.showMessageDialog(null, "Es sind folgende Fehler beim auslesen aufgetreten:" + System.getProperty("line.separator") + error, "ParseError", JOptionPane.ERROR_MESSAGE);
			sb.enterState(0);
		}
		
		
		int y = 120;		
		for(int i = 0;i < list.size();i++)
		{
			MenuItem item = list.get(i);
			item.setPos(new Vector2f(gc.getWidth()/2 - uniNormal.getWidth(item.getName())/2,y));
			y += 50;
			if(i % 9 == 0 && i != 0)
			{
				sideCount++;
				y = 120;
			}
		}
		currentIndex = 0;
		
		side = 0;
		
		start = side * 10;
		end = start + 9;
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException 
	{
		g.setAntiAlias(true);
		g.drawImage(background, gc.getWidth()/2 - background.getWidth()/2, gc.getHeight()/2 - background.getHeight()/2);
		uniTitle.drawString(gc.getWidth()/2 - uniTitle.getWidth("Level Auswahl")/2 ,(float) 0, "Level Auswahl",Color.orange);
		
		g.setColor(Color.green);
		g.draw(selection);
		
		MenuItem item;
		for(int i = start;i <= end;i++)
		{
			if(i == list.size())
			{
				itemsOnSide = i - start;
				break;
			}
			else
			{
				itemsOnSide = 10;
			}
			item = list.get(i);
			uniNormal.drawString(item.getPos().x, item.getPos().y, item.getName(),item.getColor());			
		}

		String displayside = "Seite " + (side + 1) + " von " + sideCount;		
		uniNormal.drawString(gc.getWidth()/2 - uniNormal.getWidth(displayside)/2, gc.getHeight() - uniNormal.getHeight(displayside), displayside, Color.orange);
		uniNormal.drawString(0, gc.getHeight() - uniNormal.getHeight("ESC = Zurück"), "ESC = Zurück", Color.orange);
		
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
			LevelSelection.selectedLevel = LevelManager.getInstance().getLevel(list.get(currentIndex).getName());
			sb.enterState(2);
		}
		if(input.isKeyPressed(Input.KEY_DOWN))
		{
			if(currentIndex + 2<= itemsOnSide )
				currentIndex++;
		}
		if(input.isKeyPressed(Input.KEY_UP))
		{
			if(currentIndex != 0)
				currentIndex--;
		}	
		if(input.isKeyPressed(Input.KEY_RIGHT))
		{
			if(side + 1 < sideCount)
			{
				currentIndex = 0;
				side++;
				this.start = side * 10;
				end = this.start + 9;
			}
		}
		if(input.isKeyPressed(Input.KEY_LEFT))
		{
			if(side- 1 >= 0)
			{
				currentIndex = 0;
				side--;
				this.start = side * 10;
				end = this.start + 9;
			}
		}
		int count = 0;
		for(MenuItem item : list)
		{
			if(count == currentIndex + start)
			{
				selection.setLocation(item.getPos().x - 5, item.getPos().y - 5);
				selection.setWidth(item.getPos().x + uniNormal.getWidth(item.getName()) - selection.getX() + 5);
			}
			count++;
		}
	}

	@Override
	public int getID() 
	{
		return id;
	}

}

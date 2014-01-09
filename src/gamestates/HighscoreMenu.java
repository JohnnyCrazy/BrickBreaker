package gamestates;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import main.Highscore;
import main.HighscoreManager;
import main.MenuItem;
import main.StopWatch;

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

public class HighscoreMenu extends BasicGameState
{
	int id;
	Image background;
	
	Font font;
	UnicodeFont uniTitle;
	UnicodeFont uniNormal;
	
	//Hier beginnt der recht komplizierte Teil
	
	//Momentane Auswahl in der ArrayList
	int currentIndex;
	
	//Wieviele Seiten gibt es?
	int sideCount = 0;
	
	//Ausgewählte Seite
	int side = 0;
	
	//Wieviele Items sind auf der Seite?
	int itemsOnSide = 0;
	
	//Items in der Seite, auf Seite 1: Start=1 End=10; Seite 2: Start=11 End=20
	//Startet bei 1, da 0 mit der Überschrift ([LevelName] [Time]) belegt ist
	int start;
	int end;
	
	//Speichern der highscores
	ArrayList<Highscore> highscores;
	
	//3 Spalten = 3 ArrayLists vom Typ MenuItem
	ArrayList<MenuItem> levelNames;
	ArrayList<MenuItem> playerNames;
	ArrayList<MenuItem> times;
	
	//Aktuelle Auswahl
	Rectangle selection;
	int count = 0;
	
	
	
	public HighscoreMenu(int id, Image background)
	{
		this.id = id;
		this.background = background;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame st) throws SlickException 
	{
		//Lade Pixel Font
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("./resources/pixel.ttf"));
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Große Pixel Font
		uniTitle = new UnicodeFont(font, 100, true, false);
		uniTitle.getEffects().add(new ColorEffect(java.awt.Color.orange));
		uniTitle.addAsciiGlyphs();
		uniTitle.loadGlyphs();
		
		//Kleine Pixel Font
		uniNormal = new UnicodeFont(font, 30, true, false);
		uniNormal.getEffects().add(new ColorEffect(java.awt.Color.white));
		uniNormal.addAsciiGlyphs();
		uniNormal.loadGlyphs();
		
		levelNames = new ArrayList<>();
		playerNames = new ArrayList<>();
		times =  new ArrayList<>();
		selection = new Rectangle(0, 0, 0, 40);
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame st)
	{
		//Reset aller ArrayLists
		levelNames.clear();
		playerNames.clear();
		times.clear();		
		sideCount = 1;
		
		//Auslesen und Speichern der Highscores
		HighscoreManager.getInstance().readHighscores();
		highscores = HighscoreManager.getInstance().getHighscores();
		
		//Überschriften, erkennbar an der grünen Farbe, nicht auswählbar
		MenuItem header = new MenuItem("[Spieler-Name]");
		header.setColor(Color.green);
		playerNames.add(header);
		header = new MenuItem("[Level]");
		header.setColor(Color.green);
		levelNames.add(header);
		header = new MenuItem("[Zeit]");
		header.setColor(Color.green);
		times.add(header);
		
		//Erstellung der Reihen basieren auf der Anzahl der Highscores
		for(Highscore hs : highscores)
		{
			playerNames.add(new MenuItem(hs.getPlayerName()));
			levelNames.add(new MenuItem(hs.getLevelName()));
			times.add(new MenuItem(StopWatch.getTimeAsString(hs.getTime())));
		}
		
		//Komplizierter Teil
		//Hier werden die Positionen der MenuItems gesetzt
		int y = 120;		
		for(int i = 0;i < levelNames.size();i++)
		{
			MenuItem item = levelNames.get(i);
			item.setPos(new Vector2f(gc.getWidth()/3 - 100 - uniNormal.getWidth(item.getName())/2,y));
			item = playerNames.get(i);
			item.setPos(new Vector2f(gc.getWidth()/3*2 - 80 - uniNormal.getWidth(item.getName())/2,y));
			item = times.get(i);
			item.setPos(new Vector2f(gc.getWidth()/3*2 + 200 - uniNormal.getWidth(item.getName())/2,y));
			//Erhöhung des y Wertes, da sie sonst alle den gleichen Y Wert haben würden
			y += 50;
			//Falls i / 10 keinen Rest hat, ist eine Seite voll
			//Das % bedeutet Modulo-Operator. Ein kleines Beispiel: 10 % 4 = (10 Flaschen sollen in Kästen, die 4 Flaschen fassen, verpackt werden, der Rest ist das Ergebnis(also 2))
			if(i % 10 == 0 && i != 0)
			{
				sideCount++;
				//Reset der y Variable aber + 50, da die Überschrift ja ganz oben ist
				y = 170;
			}
		}
		//Momentan ausgewählt
		currentIndex = 0;
		
		//Wo soll in der ArrayList gestartet werden? Und bis wohin? 
		start = side * 10 + 1; //Bei Seite 0: 1
		end = start + 9; //Bei Seite 0: 9
	}
	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException 
	{
		//Background etc.
		g.setAntiAlias(true);
		g.drawImage(background, gc.getWidth()/2 - background.getWidth()/2, gc.getHeight()/2 - background.getHeight()/2);
		uniTitle.drawString(gc.getWidth()/2 - uniTitle.getWidth("Highscores")/2 ,(float) 0, "Highscores",Color.orange);
		
		count = 0;
		
		//Zeichnet die Überschrift
		MenuItem item = levelNames.get(0);
		uniNormal.drawString(item.getPos().x, item.getPos().y, item.getName(),item.getColor());
		item = playerNames.get(0);
		uniNormal.drawString(item.getPos().x, item.getPos().y, item.getName(),item.getColor());
		item = times.get(0);
		uniNormal.drawString(item.getPos().x, item.getPos().y, item.getName(),item.getColor());
		
		//Von start bis zu end
		for(int i = start;i <= end;i++)
		{
			//Falls wir am ende der Liste angekommen sind, abbrechen und itemsOnSide setzen
			if(i == levelNames.size())
			{
				itemsOnSide = i - start;
				break;
			}
			//Maximale itemsOnSide = 10
			else
			{
				itemsOnSide = 10;
			}
			
			//Zeichnen der Highscores
			item = levelNames.get(i);
			uniNormal.drawString(item.getPos().x, item.getPos().y, item.getName(),item.getColor());
			item = playerNames.get(i);
			uniNormal.drawString(item.getPos().x, item.getPos().y, item.getName(),item.getColor());
			item = times.get(i);
			uniNormal.drawString(item.getPos().x, item.getPos().y, item.getName(),item.getColor());
		}

		g.setColor(Color.green);
		g.draw(selection);
		
		//aktuelle Seite zeichnen
		String displayside = "Seite " + (side + 1) + " von " + sideCount;
		uniNormal.drawString(gc.getWidth()/2 - uniNormal.getWidth(displayside)/2, gc.getHeight() - uniNormal.getHeight(displayside), displayside, Color.orange);
		
		//ESC String
		uniNormal.drawString(0, gc.getHeight() - uniNormal.getHeight("ESC = Zurück"), "ESC = Zurück", Color.orange);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException 
	{
		background.rotate(0.05F * delta);
		Input input = gc.getInput();
		
		if(input.isKeyPressed(Input.KEY_ESCAPE))
			sb.enterState(0);
		if(input.isKeyPressed(Input.KEY_DOWN))
		{
			//Wenn ausgewählter Index + 1 kleiner als die Items auf der Seite sind, erhöre den Index
			if(currentIndex + 1 < itemsOnSide )
				currentIndex++;
		}
		if(input.isKeyPressed(Input.KEY_UP))
		{
			//gleiches wie oben, nur andersrum
			if(currentIndex != 0)
				currentIndex--;
		}	
		if(input.isKeyPressed(Input.KEY_RIGHT))
		{
			//Seite wechseln
			if(side + 1 < sideCount)
			{
				currentIndex = 0;
				side++;
				start = side * 10 + 1;
				end = start + 9;
			}
		}
		if(input.isKeyPressed(Input.KEY_LEFT))
		{
			//Seite wechseln
			if(side- 1 >= 0)
			{
				currentIndex = 0;
				side--;
				start = side * 10 + 1;
				end = start + 9;
			}
		}
		count = 0;
		
		//Anpassung des Selection-Rectangles
		//Muss immer auf die Breite/Position des jeweiligen MenuItems angepasst werden
		for(MenuItem item : levelNames)
		{
			if(count == currentIndex + start)
			{
				selection.setLocation(item.getPos().x - 5, item.getPos().y - 5);
			}
			count++;
		}
		count = 0;
		for(MenuItem item : times)
		{
			if(count == currentIndex + start)
			{
				selection.setWidth(item.getPos().x + uniNormal.getWidth(item.getName()) - selection.getX() + 5);
			}
			count++;
		}
	}

	@Override
	public int getID() {
		return id;
	}

}

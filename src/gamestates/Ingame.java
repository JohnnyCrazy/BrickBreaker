package gamestates;


import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import main.Ball;
import main.Highscore;
import main.HighscoreManager;
import main.HitTypes;
import main.Level;
import main.Player;
import main.SettingsManager;
import main.StopWatch;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import powerups.None;
import powerups.Powerup;
import bricks.Brick;
import bricks.EmptyBrick;
import bricks.OneHitBrick;
import bricks.SolidBrick;

public class Ingame extends BasicGameState
{
	int id = 0;
	Image background;
	//Level, welches gespielt wird.
	Level level;
	Player ply;
	Ball ball;
	
	//Blaue Infobar am oberen Rand
	Rectangle infoBar;
	
	Font font;
	UnicodeFont uni_normal;
	UnicodeFont uni_big;
	
	GameContainer gc;
	StateBasedGame sb;
	
	//Stophr um die Zeit zu zählen
	StopWatch watch;
	
	//Ist das Spiel vorbeI?
	boolean gameEnd;
	
	//Settings
	boolean showFPS;
	boolean powerupsEnabled;
	int controls;
	
	//letzter hit bzw Block
	HitTypes lastHit = HitTypes.NONE;
	Brick lastBlock = null;
	
	//Wird bei jedem Auruf um 1 erhöht, für die Kollision wichtig
	int lastCollison = 0;

	
	
	ArrayList<Powerup> activePowerups;
	
	public Ingame(int id,Image background,GameContainer gc)
	{
		//Speicherung der Objekte
		this.id = id;
		this.background = background;
		this.gc = gc;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException 
	{
		this.sb = sb;
		//Erstellung der Objekte
		ply = new Player(100, 500, Player.WIDTH, Player.HEIGT, Color.green);
		ball = new Ball(90, 300,8);
		infoBar = new Rectangle(-5,0, gc.getWidth() + 5,30);
		font = new Font("Elephant", Font.BOLD, 20);
		
		//UnicodeFont ist eine alternative zur TrueTypeFont, welche deprecated ist
		uni_normal = new UnicodeFont(font, 20, true, false);
		uni_normal.getEffects().add(new ColorEffect());
		uni_normal.addAsciiGlyphs();
		uni_normal.loadGlyphs();
		
		//Laden der ttf Datei
		try {
			font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT,new File("./resources/pixel.ttf"));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		uni_big = new UnicodeFont(font, 40, true, false);
		uni_big.getEffects().add(new ColorEffect());
		uni_big.addAsciiGlyphs();
		uni_big.loadGlyphs();
		
		watch = new StopWatch();	
		
		activePowerups = new ArrayList<Powerup>();
	}
	
	//Wird aufgerufen, wenn der Zustand betretten wird.
	@Override
	public void enter(GameContainer gc, StateBasedGame sb)
	{
		gc.resume();
		activePowerups.clear();
		//Level aus der LevelSelection holen und aufsetzen.
		level = LevelSelection.selectedLevel;
		level.setupPositions();
		
		//Ply und Ball neu erstellen, kam zu Problemen wenn nicht.
		ply = new Player(100, 500, Player.WIDTH, Player.HEIGT, Color.green);
		ball = new Ball(90, 300,8);
		
		ply.setColor(SettingsManager.getInstance().getPlayerColor());
		ply.setLives(3);
		
		watch = new StopWatch();
		ball.setDifficultySpeed(SettingsManager.getInstance().getDifficulty());
		
		controls = SettingsManager.getInstance().getControls();
		showFPS = SettingsManager.getInstance().showFPS();
		powerupsEnabled = SettingsManager.getInstance().arePowerupsEnabled();
		
		//Mouse und Ply an Position setzen, damit das SPiel beginnen kann.
		Mouse.setCursorPosition(90, Mouse.getY());
		ply.getShape().setX(90);
		ball.setX(10);
		ball.setY(400);
		ball.setVec(1F, 1F); //Start geschwindigkeit
		lastHit = HitTypes.NONE;
		if(controls == 1) //Falls die Maus benutzt wird, Maus grabben und verbergen.
			gc.setMouseGrabbed(true);
		//Pausieren damit der Spieler nicht überrascht wird
		gc.pause();
	}	
	
	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException 
	{
		//AntiAliasing für Kantenglättung
		g.setAntiAlias(true);
		//background und infobar zeichnen
		g.drawImage(background, gc.getWidth()/2 - background.getWidth()/2, gc.getHeight()/2 - background.getHeight()/2);
		g.setColor(Color.blue);
		g.fill(infoBar);
		
		//InfoBar füllen
		g.setColor(Color.white);
		uni_normal.drawString(5, 0, "Leben:");
		uni_normal.drawString(170, 0, "Schwierigkeit: ");	
		uni_normal.drawString(335, 0, "" + SettingsManager.getInstance().getDifficultyAsString(),SettingsManager.getInstance().getDifficultyColor());
		String watchstring = watch.getTimeAsString();
		uni_normal.drawString(gc.getWidth()/2 - uni_normal.getWidth(watchstring)/2, 0, watchstring);
		uni_normal.drawString(600, 0, "Levelname: ");
		uni_normal.drawString(730, 0, level.getName(),Color.orange);
		
		//Leben des Spielers zeichnen
		g.setColor(Color.white);
		int x = 0;
		for(int i = ply.getLives();i > 0;i--)
		{
			g.fill(new Circle(100 + x, 15, 8));
			x += 8 + 15;
		}
		
		//Rechteck des SPielers zeichnen
		g.setColor(ply.getColor());
		g.fill(ply.getShape());

		//Bricks zeichnen
		for(Brick block : level.getBricks())
		{
			g.setColor(block.getColor());
			g.fill(block.getShape());
		}
		
		//Powerups
		x = 30;
		for(Powerup p : activePowerups)
		{
			if(p instanceof None)
				continue;
			//Hook auslösen
			p.onRender(gc, sb, g);
			//Falls das Powerup nicht mehr fällt und aufgesammelt wurde
			if(!p.isDropping())
			{
				g.drawImage(p.getSmallImage(),x - 6 , gc.getHeight() - 50);
				uni_normal.drawString(x, gc.getHeight() - 100, p.getCountdown().getTimeAsSeconds());
				x += 40;
			}
		}
		//Ball zeichnen
		g.setColor(ball.getColor());		
		g.fill(ball.getShape());
		
		//Pause anzeige
		if(gc.isPaused())
		{
			String pause = "PAUSE | Noch " + ply.getLives() + " Leben";
			uni_big.drawString(gc.getWidth()/2 - uni_big.getWidth(pause)/2,gc.getHeight()/2 - uni_big.getHeight(pause)/2,pause,Color.orange);
			uni_big.drawString(gc.getWidth()/2 - uni_big.getWidth("Drücke 'SPACE' um fortzufahren")/2,gc.getHeight()/2 - uni_big.getHeight("Drücke 'SPACE' um fortzufahren")/2 + 30,"Drücke 'SPACE' um fortzufahren",Color.orange);
		}
		//FPS anzeigen
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
		
		//Input Objekt für die Tastatur-Erkennung
		Input input = gc.getInput();		
		if(input.isKeyPressed(Input.KEY_SPACE))
		{
			if(gc.isPaused())
				gc.resume();
			else
				gc.pause();
		}
		//Falls ESC gedrückt, zurück ins Hauptmenü
		if(input.isKeyPressed(Input.KEY_ESCAPE))
		{
			sb.enterState(0);
			return;
		}
		
		if(gc.isPaused())
		{
			//Maus freilassen, falls Spiel pausiert.
			gc.setMouseGrabbed(false);
			//Uhr pausieren
			watch.pause();
			for(Powerup p : activePowerups)
				p.getCountdown().pause();
			//Da pause => Nichts weiter berechnen
			return;
		}
		else
		{
			if(controls == 1)
				gc.setMouseGrabbed(true);
			watch.resume();
			for(Powerup p : activePowerups)
				p.getCountdown().resume();
		}
		//Falls das Fenster nicht im Vordergrund ist
		if(!Display.isActive() || !Display.isVisible())
			gc.pause();
		
		if(!watch.isRunning())
			watch.start();
		
		//Mitberechnung des Delta-Wertes, dadurch wird das Programm CPU unabhängig.
		float delta_time =  0.1F * delta * ball.getSpeed();
		
		//Ball verschieben.
		ball.getShape().setY(ball.getShape().getY() + (ball.getVec().y * delta_time));
		ball.getShape().setX(ball.getShape().getX() + (ball.getVec().x * delta_time));
		
		if(controls == 0) //Keyboard controls
		{
			//ply.getRightKey(), da die Steuerung ja invertiert sein könnte.
			if(Keyboard.isKeyDown(ply.getRightKey()))
			{
				double change = 0.5f * delta;
				//Kann der Spieler noch weiter rechts?
				if(ply.getShape().getMaxX() < gc.getWidth())
					ply.getShape().setX((float) (ply.getShape().getX() + change));
			}
			if(Keyboard.isKeyDown(ply.getLeftKey()))
			{
				double change = 0.5f * delta;
				//Kann der Spieler noch weiter links?
				if(ply.getShape().getMinX() > 0)
					ply.getShape().setX((float) (ply.getShape().getX() - change));
			}
		}
		else // Mouse Controls
		{
			//Zurücksetzen der Maus Position, falls sie nicht im Fenster ist
			if(ply.getShape().getMaxX() >= gc.getWidth())
			{
				if(ply.isSwitched())
					Mouse.setCursorPosition((int)ply.getShape().getWidth() + 1, Mouse.getY());
				else
					Mouse.setCursorPosition(999 - (int)ply.getShape().getWidth(), Mouse.getY());
			}
			else if(ply.getShape().getMinX() <= 0 )
			{
				if(ply.isSwitched())
					Mouse.setCursorPosition(999 - (int)ply.getShape().getWidth(), Mouse.getY());
				else
					Mouse.setCursorPosition(1, Mouse.getY());
			}
			
			//Je nachdem, ob die Steurung invertiert ist, wird die Maus anders gesetzt.
			if (ply.isSwitched()) 
			{
				ply.getShape().setX(1000 - Mouse.getX());
			} else 
			{
				ply.getShape().setX(Mouse.getX());
			}
		}
		
		//Jetzt geht die Kollision los...
		//Falls der Ball den Ply auf der Oberseite berührt und nicht als letzt Kollision den Ply hatte
		if(ply.intersectsOnTop(ball.getShape()) && lastHit != HitTypes.PADDLE)
		{
			//Wie weit ist Ball vom Punkt ganz rechts des Players entfernt? In der Mitte wäre es z.B 40
			double in = ball.getShape().getCenterX() - ply.getShape().getX();
			
			// Umrechnung in ein vom Aufprall-Ort abhängige Variable (Mitte des Players: 0, ganz rechts = -1.0, links = 1.0)
			double cos = 1.0D - 2.0D * in / ply.getShape().getWidth();
			//* -1, sodass rechtsauf dem Player postiv und links negativ ist
			cos *= -1D;
			
			//Falls der Betrag größer als 0.9 sein sollte (Der Ball kann somit nicht ganz flach abgestoßen werden)
			if (Math.abs(cos) > 0.9D) 
			{
				//Minimalen Winkel nehmen
		        cos = Math.signum(cos) * 0.9D;
			}
			//Konvertierung zum Winkel (Direkt zu Radians ging nicht...)
			double out = (Math.toDegrees(Math.acos(cos)));
			//Konvertierung zu Radians
			out = Math.toRadians(out);
			//(Cosinus von diesem Wert ist dann der x Wert des Vektors)
			float x = (float) Math.cos(out);
			//Da der Ball immer nach oben gehen muss, ist es der postive Wert * -1 vom sinus
			float y = -1 * Math.abs((float) Math.sin(out));
			//Ball speed mit einberechnen
			ball.setVec(x * ball.getSpeed(), y * ball.getSpeed());		
			lastHit = HitTypes.PADDLE;
		}
		//Ablenkung je nachdem, welche Wand getroffen wurde
		if(ball.getShape().getMaxX() >= gc.getWidth() && lastHit != HitTypes.RIGHT)
		{
			ball.setVec(-ball.getVec().x,ball.getVec().y);
			lastHit = HitTypes.RIGHT;
		}
		if(ball.getShape().getMinX() < 0 && lastHit != HitTypes.LEFT)
		{
			ball.setVec(-ball.getVec().x,ball.getVec().y);
			lastHit = HitTypes.LEFT;
		}
		if(ball.getShape().getMinY() <= 30 && lastHit != HitTypes.TOP)
		{
			ball.setVec(ball.getVec().x, -ball.getVec().y);
			lastHit = HitTypes.TOP;
		}
		if(ball.getShape().getMaxY() >= gc.getHeight() && lastHit != HitTypes.BOTTOM)
		{
			if(ply.getLives() > 1)
			{
				//Zurücksetzen des ply und balls
				ply.setLives(ply.getLives() - 1);
				ball.setX(10);
				ball.setY(400);
				ball.setVec(1F, 1F);
				Mouse.setCursorPosition(90, Mouse.getY());
				ply.getShape().setX(90);
				gc.pause();
			}
			else
			{
				ply.setLives(ply.getLives() - 1);
				endGame(false);
			}
		}
		
		
		Brick collisionCheck;
		boolean shouldEndGame = true;
		for(int i = 0;i < level.getBricks().size();i++)
		{	
			//Aktueller Brick aus der Schleife
			collisionCheck = level.getBricks().get(i);
			//Falls ein temporärer Block noch existiert, nicht das game beenden
			if(!(collisionCheck instanceof EmptyBrick) && !(collisionCheck instanceof SolidBrick))
				shouldEndGame = false;
			//Block getroffen und Kollision verarbeiten
			if(lastHit != HitTypes.BLOCK || lastBlock != collisionCheck )
			{	
				//Ignorieren falls die Letzte Kollision 3 Aufrufe zurück liegt
				if(collisionCheck instanceof EmptyBrick || lastCollison < 4)
				{
					continue;					
				}
				//Wo hat der Ball den Brick getroffen? Oder berührt er den Ball gar nicht?
				ArrayList<Integer> collisionDirec = getCollisionDirec(ball.getShape(),collisionCheck.getShape());
				//Ball berührt den Brick nicht
				if(collisionDirec.size() == 0)
					continue;
				//Löse die OnHit Methode aus
				collisionCheck.onHit(ball, i, level.getBricks(),collisionDirec);
				//Falls die powerups an sind und es ein 1HIT Brick ist
				if(powerupsEnabled && collisionCheck instanceof OneHitBrick)
				{
					//Aktvier das Powerup
					collisionCheck.getpowerup().drop(this);
					collisionCheck.getpowerup().setX(collisionCheck.getShape().getX() + collisionCheck.getShape().getWidth()/2 - 25);
					collisionCheck.getpowerup().setY(collisionCheck.getShape().getY() + collisionCheck.getShape().getHeight()/2);
					activePowerups.add(collisionCheck.getpowerup());
				}
				
				lastHit = HitTypes.BLOCK;
				lastBlock = level.getBricks().get(i);
				lastCollison = 0;
			}
		}
		lastCollison++;
		
		//Game beenden und sagen, dass man das level geschafft hat
		if(shouldEndGame)
			endGame(true);
		
		
		Boolean found = false;
		
		//Iterator, da man bei einer normalen For-Schleife nicht Elemente removen kann
		for (Iterator<Powerup> it = activePowerups.iterator(); it.hasNext(); )
		{
			Powerup p = it.next();
			//Beinhaltet die ArrayList "leere" Powerups? Wenn ja, raus
			if(p instanceof None)
			{
				try
				{
					it.remove();
					return;
				} catch(IllegalStateException e)
				{
					e.printStackTrace();
				}
			}
			//Update methode auslösen
			p.onUpdate(gc, sb, delta);
			//Countdown des aufgesammelten Powerups ist ausgelaufen, deaktivieren und entfernen
			if(!p.isDropping() && p.getCountdown().isOver())
			{
				try
				{
					p.onDeactivate();
					it.remove();
					return;
				} catch(IllegalStateException e)
				{
					
				}
			}
			//Das Powerup wurde nicht aufgesammelt und ist am Boden
			if(p.isDropping() && p.getHitbox().getMaxY() > gc.getHeight())
			{
				try
				{
					it.remove();
					return;
				} catch(IllegalStateException e)
				{
					
				}
			}
			//Powerup berührt den player und wird augesammelt
			if(p.getHitbox().intersects(ply.getShape()))
			{
				//Exisitiert schon ein Powerup dieses Typs?
				for(Powerup pow : activePowerups)
				{
					if(pow.getName() == p.getName() && !pow.isDropping() && pow != p)
					{
						//Aktiviere das schon vorhandene nochmal
						pow.getCountdown().setTime(pow.getTime());
						pow.onActivate();
						p.setDropping(false);
						try
						{
							it.remove();
						}
						catch(IllegalStateException e)
						{
							e.printStackTrace();
						}
						found = true;
						break;
					}
				}
				if(!found)
				{
					//Aktiviere das neue, da kein altes vorhanden ist
					p.getCountdown().setTime(p.getTime());
					p.onActivate();
					p.setDropping(false);
				}
			}
		}
		
	}
	public void endGame(boolean won)
	{
		
		gameEnd = true;
		if(won)
		{
			//Spiel wurde gewonnen, Highscores etc werden ggf. erstellt
			long time = watch.getTime();
			Highscore current = new Highscore(level.getName(), "", time);
			if(HighscoreManager.getInstance().isBetter(current))
			{
				String name = JOptionPane.showInputDialog(null, "Herzlichen Glückwunsch! :)\n"
						+ "Du hast einen neuen Highscore aufgestellt\n"
						+ "Level: " + level.getName() + "\n"
						+ "Zeit: " + watch.getTimeAsString() + "\n"
						+ "Bitte gib deinen Namen ein:", "NEUER HIGHSCORE!", JOptionPane.INFORMATION_MESSAGE);
				if(name == "" || name == null)
				{
					sb.enterState(0);
				}
				else
				{
					current.setPlayerName(name);
					HighscoreManager.getInstance().addNewHighscore(current);
					sb.enterState(0);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Level erfolgreich bestanden!\nAber leider hast du keinen neuen Highscore aufgestellt! :(", "Level bestanden!", JOptionPane.INFORMATION_MESSAGE);
				sb.enterState(0);
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Du hast leider keine Leben mehr übrig!\nVersuche es am besten nochmal!", "Verloren!", JOptionPane.INFORMATION_MESSAGE);
			sb.enterState(0);
		}
	}

	public ArrayList<Powerup> getActivePowerups()
	{
		return activePowerups;
	}
	// Wird nicht benötigt
//	public boolean isPowerupActive(Type powerup)
//	{
//		for(Powerup p : activePowerups)
//		{
//			if(p.getClass() == powerup && !p.isDropping())
//				return true;
//		}
//		return false;
//	}
	public Player getPlayer()
	{
		return ply;
	}
	public void pause()
	{
		lastHit = HitTypes.NONE;
		gc.pause();
	}
	public Ball getBall()
	{
		return ball;
	}
	
	//Erstellt Linien für jede Seite und gibt eine ArrayList zurück
	private ArrayList<Integer> getCollisionDirec(Shape first,Shape second)
	{
		ArrayList<Integer> direc = new ArrayList<Integer>();
		Line collisionLine;
		
		//north
		collisionLine = new Line(second.getX() + 0.5F,second.getY() - 0.5F,second.getX() + second.getWidth() -1F, second.getY() - 0.5F);
		if(first.intersects(collisionLine))
		{
			if(ball.getVec().y > 0)
			{
				direc.add(0);
			}
		}
		//south
		collisionLine = new Line(second.getX(),second.getY() + second.getHeight() - 0.5F,second.getX() + second.getWidth() - 1F, second.getY() + second.getHeight() - 0.5F);
		if(first.intersects(collisionLine))
		{
			if(ball.getVec().y < 0)
			{
				direc.add(1);
			}
		}
		//east
		collisionLine = new Line(second.getX() + second.getWidth() - 0.5F, second.getY() , second.getX() + second.getWidth() - 0.5F, second.getY() + second.getHeight() - 1.5F);
		if(first.intersects(collisionLine))
		{
			if(ball.getVec().x < 0)
			{
				direc.add(2);
			}
		}
		//west
		collisionLine = new Line(second.getX() -0.5F, second.getY() + 0.5F, second.getX() - 0.5F, second.getY() + second.getHeight() - 1F);
		if(first.intersects(collisionLine))
		{
			if(ball.getVec().x > 0)
			{
				direc.add(3);
			}
		}
		return direc;
	}

	@Override
	public int getID() 
	{
		return id;
	}

}

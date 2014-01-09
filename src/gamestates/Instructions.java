package gamestates;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

//Einfachste Klasse,Zeigt die Anleitung,
public class Instructions extends BasicGameState{

	
	int id;
	Image background;
	Image instructions;
	
	UnicodeFont uniNormal;
	Font font;
	
	public Instructions(int id,Image background)
	{
		this.id = id;
		this.background = background;
	}
	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException 
	{
		instructions = new Image("./resources/anleitung.png");
		
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("./resources/pixel.ttf"));
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Kleine Pixel Font
		uniNormal = new UnicodeFont(font, 30, true, false);
		uniNormal.getEffects().add(new ColorEffect(java.awt.Color.white));
		uniNormal.addAsciiGlyphs();
		uniNormal.loadGlyphs();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)throws SlickException 
	{
		g.drawImage(background, gc.getWidth()/2 - background.getWidth()/2, gc.getHeight()/2 - background.getHeight()/2);
		g.drawImage(instructions, 0, 0);
		
		//ESC String
		uniNormal.drawString(0, gc.getHeight() - uniNormal.getHeight("ESC = Zurück"), "ESC = Zurück", Color.orange);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException 
	{
		background.rotate(0.05F * delta);
		Input input = gc.getInput();
		if(input.isKeyPressed(Input.KEY_ESCAPE))
		{
			sb.enterState(0);
			return;
		}
	}

	@Override
	public int getID() 
	{
		return id;
	}

}

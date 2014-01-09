package main;

//Reine Speicher-Klasse, besteht nur aus Getter und Setter
public class Highscore {
	
	String playername;
	String levelname;
	long time;
	
	public Highscore(String levelname,String playername,long time)
	{
		this.levelname = levelname;
		this.playername = playername;
		this.time = time;
	}
	public String getPlayerName()
	{
		return playername;
	}
	public long getTime()
	{
		return time;
	}
	public String getLevelName()
	{
		return levelname;
	}
	public void setPlayerName(String playername)
	{
		this.playername = playername;
	}

}

package main;


//Eine Util-Klasse, zählt von einem Wert runter
public class CountdownWatch 
{
	long time;
	long end_time;
	long pauseTime;
	boolean paused;
	
	public CountdownWatch(long time) 
	{
		this.time = time;
		paused = false;
	}
	public void start()
	{
		end_time = System.currentTimeMillis() + time;
	}
	//Pausier den Timer, wenn z.B das Spiel pausiert wird;
	public void pause()
	{
		if(paused || end_time == 0 || isOver())
			return;
		paused = true;
		pauseTime = end_time - System.currentTimeMillis();
	}
	//Zähl wieder runter
	public void resume()
	{
		if(!paused || end_time == 0 || isOver())
			return;
		paused = false;
		end_time = System.currentTimeMillis() + pauseTime;
	}
	//Zeit hinzufügen
	public void addTime(long add)
	{
		if(end_time < System.currentTimeMillis())
			end_time = System.currentTimeMillis();
		end_time += add;
	}
	//Falls die Zeit akualiesert werden muss
	public void setTime(long add)
	{
		time = add;
		end_time = System.currentTimeMillis() + add;
	}
	public boolean isPaused()
	{
		return paused;
	}
	public long getTime()
	{
		if(paused)
			return pauseTime;
		else
		{
			if(isOver())
				return 0;
			else
				return end_time - System.currentTimeMillis();
		}
	}
	//Ist die Zeit vorbei?
	public Boolean isOver()
	{
		if(end_time - System.currentTimeMillis() <= 0)
			return true;
		else
			return false;
	}
	//Formatiert die verbleibende Zeit, wird bei powerups verwendet
	public String getTimeAsSeconds()
	{
		if(isOver())
			return "0";
		if(paused)
		{
			return "" + (pauseTime/1000) % 60;
		}
		else
		{
			return "" + ((end_time - System.currentTimeMillis())/1000) % 60;
		}

	}
}

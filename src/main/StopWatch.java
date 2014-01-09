package main;

//Stopuhr, mit pause-Funktion etc
public class StopWatch 
{
	long time;
	long pauseTime;
	boolean paused;
	boolean running;
	
	public StopWatch()
	{
		paused = false;
	}
	public void start()
	{
		time = System.currentTimeMillis();
		running = true;
	}
	public void pause()
	{
		if(paused)
			return;
		paused = true;
		pauseTime = System.currentTimeMillis();
	}
	public void resume()
	{
		if(!paused)
			return;
		paused = false;
		time = time + (System.currentTimeMillis() - pauseTime);
	}
	public boolean isRunning()
	{
		return running;
	}
	public boolean isPaused()
	{
		return paused;
	}
	public long getTime()
	{
		if(!paused)
			return System.currentTimeMillis() - time;
		else
			return pauseTime - time;
	}
	//Darstellung als String wird zurückgegeben
	public String getTimeAsString()
	{
		long current = getTime();
		String milliseconds = "" +  current % 1000;
		String seconds = "" + (current/1000) % 60 ;
		String minutes = "" + ((current/(1000*60)) % 60);
		if(seconds.length() == 1)
			seconds = "0" + seconds;
		if(minutes.length() == 1)
			minutes = "0" + minutes;
		while(milliseconds.length() < 3)
			milliseconds = "0" + milliseconds;
		return minutes + ":" + seconds + ":" + milliseconds;
	}
	//^-> gleiche Funktion nur statisch
	public static String getTimeAsString(long time)
	{
		String milliseconds = "" +  time % 1000;
		String seconds = "" + (time/1000) % 60 ;
		String minutes = "" + ((time/(1000*60)) % 60);
		if(seconds.length() == 1)
			seconds = "0" + seconds;
		if(minutes.length() == 1)
			minutes = "0" + minutes;
		while(milliseconds.length() < 3)
			milliseconds = "0" + milliseconds;
		return minutes + ":" + seconds + ":" + milliseconds;
	}
}

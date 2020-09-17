package mkproject.maskat.ChatManager;

import mkproject.maskat.Papi.Papi;

public class TaskAsyncTimerTPS implements Runnable {
	private double LAST_TPS = 0D;
	@Override
	public void run() {
		double NOW_TPS = Papi.Function.getTPS();
		if(LAST_TPS == NOW_TPS)
			return;
		
		LAST_TPS = NOW_TPS;
		
		TabFormatter.updateTPS();
	}
}

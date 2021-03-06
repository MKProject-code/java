package mkproject.maskat.Papi;

import java.util.Timer;
import java.util.TimerTask;

import mkproject.maskat.Papi.Scheduler.PapiSchedulerManager;

public class PapiThread extends Thread {
    public void run()
    {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				PapiSchedulerManager.doCheck();
			}
		}, 0, 1000);
    }
}

package mkproject.maskat.ChatManager;

public class TaskAsyncTimerTime implements Runnable {
	@Override
	public void run() {
		TabFormatter.updateTime();
	}
}

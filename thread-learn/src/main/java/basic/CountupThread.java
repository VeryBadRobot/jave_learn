package basic;

public class CountupThread extends Thread {
	private long counter = 0;
	private volatile boolean shutdownRequested = false;

	public void shutdownRequest() {
		shutdownRequested = true;
	}

	public boolean isShutdownRequested() {
		return shutdownRequested;
	}

	public final void run() {
		try {
			while (!shutdownRequested) {
				doWork();
			}
		} catch (InterruptedException e) {
		} finally {
			doShutdown();
		}
	}

	private void doWork() throws InterruptedException {
		counter++;
		System.out.println("doWork: counter = " + counter);
		Thread.sleep(500);
	}

	private void doShutdown() {
		System.out.println("doShutdown: counter = " + counter);
	}

	public static void main(String[] args) {
		System.out.println("main: BEGIN");
		try {
			CountupThread thread = new CountupThread();
			thread.start();
			Thread.sleep(10000);
			System.out.println("main: shutdownRequest");
			thread.shutdownRequest();
			System.out.println("main: join");
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("main: END");
	}

}

package thinkinjava;

public class LiftOff implements Runnable {
	protected int countDown = 10;

	static int taskCount = 0;

	private final int id = taskCount++;

	public LiftOff() {
	}


	public LiftOff(int countDown) {
		this.countDown = countDown;
	}

	private String status() {
		return "#" + id + "(" + (countDown > 0 ? countDown : "liftOff!") + ") , ";
	}

	@Override
	public void run() {
		while (countDown-- > 0) {
			System.out.print(status());
			Thread.yield();
		}
	}

}

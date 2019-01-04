package thinkinjava;

class Sleeper extends Thread {
	private int duration;

	public Sleeper(String name, int sleepTime) {
		super(name);
		duration = sleepTime;
		start();
	}


	@Override
	public void run() {
		try {
			sleep(duration);
		} catch (InterruptedException e) {
			System.out.println(getName() + " was interrupted. " + "isInterrupted(): " +
					isInterrupted());
			return;
		}
		System.out.println(getName() + " has awakened");
	}
}

class Joiner extends Thread {
	private Sleeper sleeper;

	public Joiner(String name, Sleeper sleeper) {
		super(name);
		this.sleeper = sleeper;
		start();
	}


	@Override
	public void run() {
		try {
			sleeper.join();
		} catch (InterruptedException e) {
			System.out.println("Interrupted");
		}
		System.out.println(getName() + " join completed");
	}
}


public class Joining {

	public static void main(String[] args) {
		Sleeper sleeper = new Sleeper("sleeper", 1500),
				grumy = new Sleeper("Grumpy", 1500);
		Joiner dopey = new Joiner("Dopey", sleeper),
				doc = new Joiner("Doc",grumy);
		grumy.interrupt();
		dopey.interrupt();
	}
}

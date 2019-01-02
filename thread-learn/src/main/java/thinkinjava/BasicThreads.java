package thinkinjava;

public class BasicThreads {

	public static void main(String[] args) {
		LiftOff liftOff = new LiftOff();

		new Thread(liftOff).start();

		System.out.println("waiting for liftOff!");
	}
}

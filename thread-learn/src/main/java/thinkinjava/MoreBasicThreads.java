package thinkinjava;

public class MoreBasicThreads {

	public static void main(String[] args) {
//		for (int i = 0; i < 5; i++) {
//			new Thread(new LiftOff()).start();
//		}
		for (int i = 0 ; i< 5; i++)
		{
			new Thread(()->new LiftOff().run()).start();
		}

		System.out.println("waiting for liftOff!");
	}
}

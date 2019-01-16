package thinkinjava;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test implements Runnable {

	private volatile int i = 0;


	private int getValue() {
		return i;
	}


	private synchronized void evenIncrement() {
		i++;
//		i++;
	}

	@Override
	public void run() {
		while (true)
			evenIncrement();
	}

	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		Test test = new Test();

		Boolean err = null;

	}




}

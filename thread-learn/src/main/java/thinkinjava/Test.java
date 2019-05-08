package thinkinjava;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

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
		//compute(19_000);
		Float dt = 0.2f;

		System.out.println();
	}


	static int compute(int n) {
		if (n == 1)
			return 1;
		else {
			return n * compute(n - 1);
		}
	}

	static int compute(int n, int result) {
		if (n == 1)
			return result;
		else {
			return compute(n - 1, n * result);
		}
	}


}

package juc;

import java.util.concurrent.CountDownLatch;

class Worker implements Runnable {
	private final CountDownLatch startSignal;
	Worker(CountDownLatch startSignal)
	{
		this.startSignal = startSignal;
	}

	@Override
	public void run() {
		try {
			startSignal.await(); // 所有执行线程在此处等待开关开启
			dowork();
		}catch (InterruptedException ex){

		}
	}

	void dowork(){
	}
}

public class Driver {
	private static final int N = 10;

	public static void main(String[] args) {
		CountDownLatch switcher = new CountDownLatch(1);
		for (int i = 0; i<N; i++)
		{
			new Thread(new Worker(switcher)).start();
		}

		doSomething();
		switcher.countDown();
	}

	public static void doSomething() {
	}
}



package juc;

import java.util.concurrent.DelayQueue;

public class DataConsumer implements Runnable {
	private final DelayQueue<Data> queue;

	public DataConsumer(DelayQueue<Data> queue) {
		this.queue = queue;
	}


	public void run() {
		while (true) {
			try {
				Data data = queue.take();
				System.out.println(Thread.currentThread().getName() + ": take " + data);

				Thread.yield();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

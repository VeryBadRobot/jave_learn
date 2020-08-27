package juc;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ThreadLocalRandom;

public class DataProducer implements Runnable {
	private final DelayQueue<Data> queue;

	public DataProducer(DelayQueue<Data> queue) {
		this.queue = queue;
	}


	public void run() {
		while (true) {
			long currentTime = System.nanoTime();
			long validTime = ThreadLocalRandom.current().nextLong(1000000000L, 7000000000L);
			Data data = new Data(currentTime + validTime);
			queue.put(data);

			System.out.println(Thread.currentThread().getName() + ": put " + data);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		DelayQueue<Data> queue = new DelayQueue<>();
		Thread c1 = new Thread(new DataConsumer(queue), "consumer-1");
		Thread p1 = new Thread(new DataProducer(queue), "producer-1");

		c1.start();
		p1.start();
	}
}

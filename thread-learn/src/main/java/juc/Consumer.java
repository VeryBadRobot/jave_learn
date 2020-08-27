package juc;

import java.util.concurrent.Exchanger;

public class Consumer implements Runnable {
	private final Exchanger<Message> exchanger;

	public Consumer(Exchanger<Message> exchanger) {
		this.exchanger = exchanger;
	}

	public void run() {
		Message msg = new Message(null);
		while (true) {
			try {
				Thread.sleep(1000);
				msg = exchanger.exchange(msg);
				System.out.println(Thread.currentThread().getName() + ":消费了数据[" + msg.getV() + "]");
				msg.setV(null);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

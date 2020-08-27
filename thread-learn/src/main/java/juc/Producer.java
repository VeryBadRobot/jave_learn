package juc;


import java.util.concurrent.Exchanger;

class Message {
	String v;


	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	Message(String v) {
		this.v = v;
	}

}

public class Producer implements Runnable {
	private final Exchanger<Message> exchanger;

	public Producer(Exchanger<Message> exchanger) {
		this.exchanger = exchanger;
	}

	@Override
	public void run() {
		Message message = new Message(null);
		for (int i = 0; i < 3; i++) {
			try {
				Thread.sleep(1000);
				message.setV(String.valueOf(i));
				System.out.println(Thread.currentThread().getName() + ": 生产了数据[" + i + "]");
				message = exchanger.exchange(message);
				System.out.println(Thread.currentThread().getName() + ":交换得到数据[" + String.valueOf(message.getV()) + "]");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	public static void main(String[] args) {
		Exchanger<Message> exchanger = new Exchanger<>();
		Thread t1 = new Thread(new Consumer(exchanger), "消费者-t1");
		Thread t2 = new Thread(new Producer(exchanger), "生产者-t2");
		t1.start();
		t2.start();
	}
}

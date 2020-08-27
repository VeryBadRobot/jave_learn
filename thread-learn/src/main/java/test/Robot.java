package test;

import java.util.concurrent.CountDownLatch;

public class Robot {
	private int i = 0;


	public void add() {
		++i;
	}


	public int getI() {
		return i;
	}

	public static void main(String[] args) throws InterruptedException {

		final PinLock lock = new PinLock();
		CountDownLatch latch = new CountDownLatch(10);
		Robot robot = new Robot();
		for (int i = 0; i < 10; i++) {
			new Thread() {

				@Override
				public void run() {
					lock.lock();
					for (int j = 0; j < 1000; j++) {
						robot.add();
					}
					lock.unLock();
					latch.countDown();
				}
			}.start();
		}

		latch.await();
		System.out.println(robot.getI());
	}


}

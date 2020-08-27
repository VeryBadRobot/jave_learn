package juc;

import java.util.concurrent.Phaser;

class PhaserTask3 implements Runnable {
	private final Phaser phaser;

	PhaserTask3(Phaser phaser) {
		this.phaser = phaser;
	}

	@Override
	public void run() {
		while (!phaser.isTerminated()) //只要Phaser没有终止，各个线程的任务就会一直执行
		{
			int i = phaser.arriveAndAwaitAdvance();   //等待其他参与者线程到达
			// do something
			System.out.println(Thread.currentThread().getName() + ": 执行完任务");

		}
	}
}

public class PhaserTest3 {

	public static void main(String[] args) {
		int repeats = 3;         // 指定任务最多执行的次数

		Phaser phaser = new Phaser() {
			protected boolean onAdvance(int phase, int registeredParties) {
				System.out.println("----------------PHASE[" + phase + "], Parties[" + registeredParties + "] -------------");
				return phase + 1 >= repeats || registeredParties == 0;
			}
		};

		for (int i = 0; i < 10; i++) {
			phaser.register();              //注册各个参与者线程
			new Thread(new PhaserTask3(phaser), "Thread-" + i).start();
		}


	}


}

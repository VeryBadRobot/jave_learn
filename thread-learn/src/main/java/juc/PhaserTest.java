package juc;

import java.util.concurrent.Phaser;

class PhaserTask implements Runnable {
	private final Phaser phaser;

	PhaserTask(Phaser phaser)
	{
		this.phaser = phaser;
	}

	@Override
	public void run() {
		int i = phaser.arriveAndAwaitAdvance();  //等待其他参与者线程到达
		//do something
		System.out.println(Thread.currentThread().getName() + ": 执行完任务，当前phase =" + i + "");
	}
}


public class PhaserTest {
	public static void main(String[] args) {
		Phaser phaser = new Phaser();
		for (int i = 0; i < 10; i++)
		{
			phaser.register();//注册各个参与者线程
			new Thread(new PhaserTask(phaser),"Thread-" + i).start();
		}



	}
}

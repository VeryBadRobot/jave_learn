package juc;

import java.util.concurrent.Phaser;

class PhaserTask4 implements Runnable {
	private final Phaser phaser;

	PhaserTask4(Phaser phaser) {
		this.phaser = phaser;
		this.phaser.register();
	}

	@Override
	public void run() {
		while (!phaser.isTerminated()) { // 只要phaser没有终止，各个线程的任务就会一直执行
			int i = phaser.arriveAndAwaitAdvance();
			// do something
			System.out.println(Thread.currentThread().getName() + ":执行完任务");
		}
	}
}

public class PhaserTest4 {
	private static final int TASKS_PER_PHASER = 4;   // 每个Phaser对象对应的工作线程（任务）数

	public static void main(String[] args) {
		int repeats = 3;   //指定任务最多执行的次数
		Phaser phaser = new Phaser() {
			protected boolean onAdvance(int phase, int registeredParties) {
				System.out.println("---------------PHASE[" + phase + "],Parties[" + registeredParties + "]");
				return phase + 1 >= repeats || registeredParties == 0;
			}
		};

		PhaserTask4[] task4s = new PhaserTask4[10];
		build(task4s, 0, task4s.length, phaser);            //根据任务数，为每个任务分配Phaser对象
		for (int i = 0; i < task4s.length; i++) {
			Thread thread = new Thread(task4s[i]);
			thread.start();
		}
	}


	private static void build(PhaserTask4[] taskers, int lo, int hi, Phaser phaser) {
		if (hi - lo > TASKS_PER_PHASER) {
			for (int i = lo; i < hi; i += TASKS_PER_PHASER)
			{
				int j = Math.min(i + TASKS_PER_PHASER, hi);
				build(taskers,i,j,new Phaser(phaser));
			}
		}else {
			for (int i = lo; i < hi; ++i)
			{
				taskers[i] = new PhaserTask4(phaser);
			}
		}
	}
}

package juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CycliBarrierTest {

	public static void main(String[] args) throws InterruptedException {
		int N = 5; //运动员数
		CyclicBarrier cb = new CyclicBarrier(N, new Runnable() {
			@Override
			public void run() {
				System.out.println("****** 所有运动员已准备完毕，发令枪：跑！ ******");
			}
		});

		List<Thread> list = new ArrayList<>();
		for (int i = 0; i < N; i++) {
			Thread t = new Thread(new PrepareWork(cb), "运动员[" + i + "]");
			t.start();
//			if (i == 3) {
//				t.interrupt();
//			}
		}
		Thread.sleep(2000);
		System.out.println("Barrier是否损坏： " + cb.isBroken());
	}

	private static class PrepareWork implements Runnable {
		private CyclicBarrier cb;

		PrepareWork(CyclicBarrier cb) {
			this.cb = cb;
		}

		@Override
		public void run() {
			try {
				System.out.println(Thread.currentThread().getName() + ": 准备完成");
				if (Thread.currentThread().getName().contains("3"))
				{
					throw new InterruptedException();
				}
				cb.await();
				System.out.println(Thread.currentThread().getName()+ "唤醒");
			} catch (InterruptedException e) {
				System.out.println(Thread.currentThread().getName() + ": 被中断");
			} catch (BrokenBarrierException e) {
				System.out.println(Thread.currentThread().getName() + " : 抛出BrokenBarrierException");
			}

		}
	}
}

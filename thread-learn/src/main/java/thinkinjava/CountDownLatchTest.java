package thinkinjava;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchTest {

	public static void main(String[] args) {
		int threadCount = 10;


		CountDownLatch countDownLatch = new CountDownLatch(10);

		for (int i =0 ; i < threadCount; ++i)
		{
			new Thread(()->{
				System.out.println("线程 ：" + Thread.currentThread().getId() + " 开始出发 " );
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("线程 ：" + Thread.currentThread().getId()+" 完成了");
				countDownLatch.countDown();
			}).start();
		}

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("10 个线程已执行完毕，开始计算排名 " );
	}

}

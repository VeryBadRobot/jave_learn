package executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadExecutorDemo {

	public static void main(String[] args) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();

		for (int i = 0; i < 5; i++) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName());
				}
			});
		}

		executorService.shutdown();
	}

}

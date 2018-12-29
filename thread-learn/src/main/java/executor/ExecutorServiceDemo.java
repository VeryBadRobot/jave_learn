package executor;

import java.util.concurrent.*;

public class ExecutorServiceDemo {

	public static void main(String[] args) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		//5个任务
		for (int i = 0; i < 5; i++) {
			executorService.submit(() -> System.out.println(Thread.currentThread().getName() + " doing task"));
		}

		executorService.shutdown();

	}

}

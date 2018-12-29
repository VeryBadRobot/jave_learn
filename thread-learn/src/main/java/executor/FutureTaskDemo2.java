package executor;

import org.omg.CORBA.TIMEOUT;

import java.util.concurrent.*;

public class FutureTaskDemo2 {

	public static void main(String[] args) throws InterruptedException {
		FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				System.out.println("futureTask is working 1+1!");
				TimeUnit.SECONDS.sleep(2);
				return 2;
			}
		});

		ExecutorService executorService = Executors.newCachedThreadPool();

		executorService.submit(futureTask);
		executorService.shutdown();

		while (!futureTask.isDone()) {
			System.out.println("子线程还没做完，我再睡会");
			TimeUnit.SECONDS.sleep(1);
		}

		try {
			System.out.println("子线程运行的结果： " + futureTask.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

}

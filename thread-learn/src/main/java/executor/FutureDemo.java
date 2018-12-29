package executor;

import java.util.concurrent.*;

public class FutureDemo {

	public static void main(String[] args) throws InterruptedException {
		//新建一个callable任务
		Callable<Integer> callableTask = new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				System.out.println("Calculating 1+1！");
				TimeUnit.SECONDS.sleep(2);//休眠2秒
				return 2;
			}
		};

		ExecutorService executorService = Executors.newCachedThreadPool();
		Future<Integer> result = executorService.submit(callableTask);
		executorService.shutdown();

		//isDone()方法可以查询子线程是否做完
		while (!result.isDone())
		{
			System.out.println("子线程正在执行");
			//休眠1秒
			TimeUnit.SECONDS.sleep(1);
		}


		try
		{
			System.out.println("子线程执行结果：" +result.get());
		}catch (InterruptedException|ExecutionException e)
		{
			e.printStackTrace();
		}
	}
}

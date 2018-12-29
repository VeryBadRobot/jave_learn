package executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

class CalcTask implements Callable<String> {
	@Override
	public String call() {
		return Thread.currentThread().getName();
	}
}


public class SubmitDemo {

	public static void main(String[] args) {
		ExecutorService executorService = Executors.newCachedThreadPool();

		List<Callable<String>> taskList = new ArrayList<>();

		//任务列表中添加5个任务
		for (int i = 0; i < 5; i++) {
			taskList.add(new CalcTask());
		}

		//结果列表，存放任务列表的返回值
		List<Future<String>> resultList = new ArrayList<>();

		try {
			//invokeAll批量运行所有任务，submit提交单个任务
			resultList = executorService.invokeAll(taskList);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			//future中输出每个任务的返回值
			for (Future<String> future : resultList) {
				//get方法会阻塞直到结果返回
				System.out.println(future.get());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		executorService.shutdown();

	}
}

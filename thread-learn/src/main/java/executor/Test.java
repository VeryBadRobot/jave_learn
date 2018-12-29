package executor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class Test {


	public static void main(String[] args) throws ExecutionException, InterruptedException {
		CompletableFuture<String> completableFuture = new CompletableFuture<>();
		new Thread(() -> {
			// 模拟执行耗时任务
			System.out.println("task doing...");
			try {
				Thread.sleep(3000);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 告诉completableFuture任务已经完成
			completableFuture.complete("ok");
		}).start();
		// 获取任务结果，如果没有完成会一直阻塞等待
		String result = completableFuture.get();
		System.out.println("计算结果:" + result);

	}

}

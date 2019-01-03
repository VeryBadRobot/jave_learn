package thinkinjava;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadReuse extends Thread {

	Queue<Callable> taskQueue = new LinkedList<>();


	public void submit(Runnable runnable)
	{
		taskQueue.offer(Executors.callable(runnable));
	}


	public void submit(Callable<?> callable)
	{
		taskQueue.offer(callable);
	}

	@Override
	public void run() {
		while (true)
		{
			if (taskQueue.peek()!=null)
			{
				try {
					Object call = taskQueue.poll().call();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}


		}
	}
}

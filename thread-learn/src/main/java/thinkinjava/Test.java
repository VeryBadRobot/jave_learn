package thinkinjava;

import java.util.concurrent.TimeUnit;

public class Test {


	public static void main(String[] args) throws InterruptedException {
		ThreadReuse threadReuse = new ThreadReuse();

		threadReuse.submit(()-> System.out.println("First runnable"));
		threadReuse.submit(()->{
			System.out.println("Second callable");
			return "";
		});

		threadReuse.start();
		TimeUnit.SECONDS.sleep(5);

		threadReuse.submit(()-> System.out.println("Third runnable"));
	}
}

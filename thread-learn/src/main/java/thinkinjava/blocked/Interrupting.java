package thinkinjava.blocked;


import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


//只有sleep阻塞可以被中断。IO阻塞和synchronized,lock均不能被中断

class SleepBlocked implements Runnable {
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(100);
		} catch (InterruptedException e) {
			System.out.println("InterruptedException");
		}
		System.out.println("Exiting SleeepBlocked.run");
	}
}

class IOBlocked implements Runnable {
	private InputStream in;

	public IOBlocked(InputStream is) {
		in = is;
	}

	@Override
	public void run() {
		try {
			System.out.println("waiting for read");
			in.read();
		} catch (IOException e) {
			if (Thread.currentThread().isInterrupted()) {
				System.out.println("Interrupted from blocked I/O");
			} else {
				throw new RuntimeException(e);
			}
		}
		System.out.println("Exiting IOBlocked.run()");
	}
}

class SynchronizedBlocked implements Runnable {
	public synchronized void f() {
		while (true)//Never release lock
		{
			Thread.yield();
		}
	}


	public SynchronizedBlocked() {
		new Thread(() -> {
			f(); //Lock acquired by this thread
		}).start();
	}

	public void run() {
		System.out.println("Trying to call f()");
		f();
		System.out.println("Exiting SynchronizedBlocked.run()");
	}
}


class LockBlocked implements Runnable{

	private Lock lock;

	void f(){
		lock.lock();
		while (true)
		{
			Thread.yield();
		}
	}

	public LockBlocked()
	{
		lock = new ReentrantLock();
		new Thread(() -> {
			f(); //Lock acquired by this thread
		}).start();
	}

	@Override
	public void run() {
		System.out.println("start f");
		f();
		System.out.println("exit");

	}
}

public class Interrupting {
	private static ExecutorService exec = Executors.newCachedThreadPool();

	static void test(Runnable r)throws Exception {
		Future<?> f = exec.submit(r);
		TimeUnit.MILLISECONDS.sleep(100);
		System.out.println("Interrupting " + r.getClass().getName());
		f.cancel(true);// Interrupts if running
		System.out.println("Interrupt sent to " + r.getClass().getName());
	}


	public static void main(String[] args) throws Exception {
		test(new SleepBlocked());
		test(new IOBlocked(System.in));
		test(new SynchronizedBlocked());
		test(new LockBlocked());
		TimeUnit.SECONDS.sleep(3);
		System.out.println("Aborting with System.exit(0)");
		System.exit(0); // ... since last 2 interrupts failed
	}

}

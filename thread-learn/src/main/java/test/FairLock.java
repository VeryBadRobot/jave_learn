package test;

import java.util.concurrent.atomic.AtomicInteger;

public class FairLock {
	private AtomicInteger serviceNum = new AtomicInteger(0);
	private AtomicInteger currentNum = new AtomicInteger(0);

	public void lock()
	{
		int cur = currentNum.getAndIncrement();
		while (serviceNum.get()!=cur)
		{}

	}


	public void unlock(int myticket){
		int next = myticket + 1;
		serviceNum.compareAndSet(myticket,next);
	}

}

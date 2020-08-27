package test;

import java.util.concurrent.atomic.AtomicBoolean;

public class PinLock {

	private volatile AtomicBoolean lock = new AtomicBoolean(false);


	public void lock() {
		while (!lock.compareAndSet(false, true)) {
		}
	}

	public void unLock() {
		lock.compareAndSet(true, false);
	}
}

package test;

import java.util.concurrent.Semaphore;

class Pool {
	private static final int MAX_AVAILABLE = 100; //可同时访问资源的最大线程数
	private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);
	protected Object[] items = new Object[MAX_AVAILABLE];   //共享资源
	protected boolean[] used = new boolean[MAX_AVAILABLE];

	public Object getItem() throws InterruptedException {
		available.acquire();
		return getNextAvailableItem();
	}

	public void putItem(Object x) {
		if (markAsUnused(x)) {
			available.release();
		}
	}

	private synchronized Object getNextAvailableItem() {
		for (int i = 0; i < MAX_AVAILABLE; ++i) {
			if (!used[i]) {
				used[i] = true;
				return items[i];
			}
		}
		return null;
	}

	private synchronized boolean markAsUnused(Object item) {
		for (int i = 0; i < MAX_AVAILABLE; ++i) {
			if (item == items[i]) {
				if (used[i]) {
					used[i] = false;
					return true;
				} else
					return false;
			}
		}
		return false;
	}

}

public class OfficalExample {
}

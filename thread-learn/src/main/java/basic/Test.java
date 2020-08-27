package basic;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

class Point {
	private double x, y;
	private final StampedLock s1 = new StampedLock();

	void move(double deltaX, double deltaY) {
		long stamp = s1.writeLock(); //涉及对共享资源的修改，使用写锁-独占操作
		try {
			x += deltaX;
			y += deltaY;
		} finally {
			s1.unlockWrite(stamp);
		}
	}

	double distanceFromOrigin() {
		long stamp = s1.tryOptimisticRead(); //使用乐观读锁
		double currentX = x, currentY = y; // 拷贝共享资源到本地方法栈中
		if (!s1.validate(stamp)) {           // 如果有写锁被占用，可能造成数据不一致，所以要切换到普通读锁
			stamp = s1.readLock();
			try {
				currentX = x;
				currentY = y;
			} finally {
				s1.unlockRead(stamp);
			}
		}

		return Math.sqrt(currentX * currentX + currentY * currentY);
	}

	void moveIfAtOrigin(double newX, double newY) {
		// upgrade
		// Could instead start with optimistic, not read mode
		long stamp = s1.readLock();
		try {
			while (x == 0.0 && y == 0.0) {
				long ws = s1.tryConvertToWriteLock(stamp);  // 读锁转换为写锁
				if (ws != 0L) {
					stamp = ws;
					x = newX;
					y = newY;
					break;
				} else {
					s1.unlockRead(stamp);
					stamp = s1.writeLock();
				}
			}
		} finally {
			s1.unlock(stamp);
		}
	}
}

public class Test {
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

	private final Lock r = rwl.readLock();

	private final Lock w = rwl.writeLock();

	{
		r.lock();
		w.lock();
		r.unlock();
	}


	public static void main(String[] args) {
		int count = 1000_000;
		testA(count);
		testB(count);

	}

	static void testA(int count) {
		long start = System.currentTimeMillis();
		Set<Integer> set = new HashSet<>();

		for (int i = 0; i < count; i++) {
			set.add(i);
		}

		System.out.println("A cost: " + (System.currentTimeMillis() - start));
	}


	static void testB(int count) {
		long start = System.currentTimeMillis();
		int test = (count * 7) / 10;
		Set<Integer> set = new HashSet<Integer>(test);

		for (int i = 0; i < count; i++) {
			set.add(i);
		}

		System.out.println("B cost: " + (System.currentTimeMillis() - start));
	}

}

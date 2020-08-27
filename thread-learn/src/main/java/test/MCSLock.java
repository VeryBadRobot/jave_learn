package test;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class MCSLock {
	public static class MCSNode {
		MCSNode next;
		boolean isLocked = true;  // 默认是在等待锁
	}

	volatile MCSNode queue; // 指向最后一个申请锁的MCSNode
	private static final AtomicReferenceFieldUpdater<MCSLock, MCSNode> UPDATER = AtomicReferenceFieldUpdater.newUpdater(MCSLock.class, MCSNode.class, "queue");

	public void lock(MCSNode currentThread) {
		MCSNode predecessor = UPDATER.getAndSet(this, currentThread);  // step 1
		if (predecessor != null) {
			predecessor.next = currentThread; // step 2

			while (currentThread.isLocked) { // step 3
			}
		}
	}

	public void unlock(MCSNode currentThread) {
		if (UPDATER.get(this) == currentThread) { // 锁拥有者释放锁才有意义
			if (currentThread.next == null) { // 检查是否有人排在自己后面
				if (UPDATER.compareAndSet(this, currentThread, null)) {
					//compareAndSet返回true表示确实没有人排在自己后面
					return;
				} else {
					// 突然有人排在自己后面了，可能还不知道是谁，下面是等待后续者
					// 这里之所以要忙等是因为：step 1执行完后，step 2可能还没执行完
					while (currentThread.next == null) { // step 5

					}
				}
			}
			currentThread.next.isLocked = false;
			currentThread.next = null;   // for GC
		}

	}


}

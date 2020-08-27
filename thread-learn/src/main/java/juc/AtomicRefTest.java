package juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

class Task implements Runnable {
	private AtomicReference<Integer> ref;

	Task(AtomicReference<Integer> ref) {
		this.ref = ref;
	}

	@Override
	public void run() {
		for (; ; ) { //自旋操作
			Integer oldV = ref.get();
			if (ref.compareAndSet(oldV, oldV + 1)) {
				break;
			}
		}
	}
}


public class AtomicRefTest {
	private Integer integer;

	public static void main(String[] args) throws InterruptedException {

		Boolean b = null;
		if (b)
		{
			System.out.println('f');
		}





	}
}

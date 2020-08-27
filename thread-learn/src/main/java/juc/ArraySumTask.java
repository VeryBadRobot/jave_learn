package juc;

import java.util.concurrent.*;

public class ArraySumTask extends RecursiveTask {
	private final int[] array;
	private final int begin;
	private final int end;

	private static final int THRESHOLD = 100;

	public ArraySumTask(int[] array, int begin, int end) {
		this.array = array;
		this.begin = begin;
		this.end = end;
	}


	@Override
	protected Object compute() {
		long sum = 0;
		if (end - begin + 1 < THRESHOLD) {
			for (int i = begin; i <= end; i++) {
				sum += array[i];
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
			}

		} else {
			int middle = (begin + end) / 2;
			ArraySumTask subtask1 = new ArraySumTask(this.array, begin, middle);
			ArraySumTask subtask2 = new ArraySumTask(this.array, middle + 1, end);

			subtask1.fork();
			subtask2.fork();

			long sum1 = (long) subtask1.join();
			long sum2 = (long) subtask2.join();

			sum = sum1 + sum2;
		}
		return sum;
	}


	public static void main(String[] args) {
		ForkJoinPool executor = new ForkJoinPool();
		ArraySumTask task = new ArraySumTask(new int[400], 0, 399);
		long start =System.currentTimeMillis();

		executor.invoke(task);
		System.out.println(System.currentTimeMillis()-start);

	}
}

package juc;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Data implements Delayed {
	private static final AtomicLong atomic = new AtomicLong(0);
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss-n");
	// 数据的失效时间点
	private final long time;

	// 序号
	private final long seqno;

	public Data(long deadline) {
		this.time = deadline;
		this.seqno = atomic.getAndIncrement();
	}

	/**
	 * 返回剩余有效时间
	 *
	 * @param unit
	 * @return
	 */
	public long getDelay(TimeUnit unit) {
		return unit.convert(this.time - System.nanoTime(), TimeUnit.NANOSECONDS);
	}


	public int compareTo(Delayed other) {
		if (other == this) // compare zero if same object
			return 0;

		if (other instanceof Data) {
			Data x = (Data) other;

			// 优先比较失效时间
			long diff = this.time - x.time;
			if (diff < 0)
				return -1;
			else if (diff > 0)
				return 1;
				//剩余时间相同则比较序号
			else if (this.seqno < x.seqno)
				return -1;
			else
				return 1;
		}
		// 一般不会执行到此处，除非元素不是Data类型
		long diff = this.getDelay(TimeUnit.NANOSECONDS) - other.getDelay(TimeUnit.NANOSECONDS);
		return (diff < 0) ? -1 : (diff > 0) ? 1 : 0;
	}

	public String toString() {
		return "Data{" + "time=" + time + ", seqno=" + seqno +
				"}, isValid=" + isValid();
	}

	private boolean isValid() {
		return this.getDelay(TimeUnit.NANOSECONDS) > 0;
	}
}

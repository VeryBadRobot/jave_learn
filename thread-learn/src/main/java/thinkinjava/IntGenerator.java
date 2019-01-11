package thinkinjava;

public abstract class IntGenerator {
	private volatile boolean canceled = false;

	public abstract int next();

	//allow this to be cancel()
	public void cancel() {
		canceled = true;
	}

	public boolean isCanceled() {
		return canceled;
	}
}

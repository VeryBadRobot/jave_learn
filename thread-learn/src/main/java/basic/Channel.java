package basic;

public class Channel {
	private static final int MAX_REQUEST = 100; //最大请求数
	private final Request[] requestQueue; // 请求队列
	private int tail;
	private int head;
	private int count;
	private final WorkerThread[] threadPool;

	public Channel(int threads) {
		this.requestQueue = new Request[MAX_REQUEST];
		this.head = 0;
		this.tail = 0;
		this.count = 0;
		threadPool = new WorkerThread[threads];
		for (int i = 0; i < threadPool.length; i++) {
			threadPool[i] = new WorkerThread("Worker-" + i, this);
		}
	}

	public void startWorkers() {
		for (int i = 0; i < threadPool.length; i++) {
			threadPool[i].start();
		}
	}

	public synchronized void putRequest(Request request) {
		while (count >= requestQueue.length) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		requestQueue[tail] = request;
		tail = (tail + 1) % requestQueue.length;
		count++;
		notifyAll();
	}

	public synchronized Request takeRequest() {
		while (count < 1) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		Request request = requestQueue[head];
		head = (head + 1) % requestQueue.length;
		count--;
		notifyAll();
		return request;
	}

	public static void main(String[] args) {
		Channel channel = new Channel(5);
		channel.startWorkers();
		new ClientThread("Alice", channel).start();
		new ClientThread("Bobby", channel).start();
		new ClientThread("Chris", channel).start();
	}

}

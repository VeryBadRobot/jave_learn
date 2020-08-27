package basic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;

public class Host {
	private final Helper helper = new Helper();

	public void request(final int count, final char c) {
		System.out.println("      request(" + count + ", " + c + ") BEGIN");
		new Thread() {
			@Override
			public void run() {
				helper.handle(count, c);
			}
		}.start();
		System.out.println("     request(" + count + ", " + c + ") END");
	}


	public static void main(String[] args) {

		System.out.println("Main begin");
		Host host = new Host();
		host.request(10, 'A');
		host.request(20, 'B');
		host.request(30, 'C');
		System.out.println("Main end");
	}
}

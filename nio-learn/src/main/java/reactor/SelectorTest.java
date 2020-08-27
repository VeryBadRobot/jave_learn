package reactor;

import java.io.IOException;
import java.nio.channels.Selector;

public class SelectorTest {
	public static final int MAX_SIZE = 5;





	public static void main(String[] args) {
		Selector[] selectors = new Selector[MAX_SIZE];

		try {
			for (int i = 0; i < MAX_SIZE; i++) {

				selectors[i] = Selector.open();

			}
			Thread.sleep(30000);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}


}

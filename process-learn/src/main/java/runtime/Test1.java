package runtime;

import java.util.ArrayList;
import java.util.List;

public class Test1 extends Thread {
	List<String> strs = new ArrayList<>();

	public static void main(String args[]) {
		for (int i = 0; i < 100; i++) {
			new Test1().start();
		}



	}

	@Override
	public void run() {
		delete(strs);
	}

	public void delete(List<String> strs) {
		strs.add("123");
		strs.add("456");
		strs.add("789");
		int n = 0;
		for (int i = 0; i < (n =strs.size()); i++) {
			strs.remove(i);
			System.out.println("n = " + n);
		}
		//System.out.println(strs.size());
	}
}
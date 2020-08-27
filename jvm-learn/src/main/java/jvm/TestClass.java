package jvm;

import java.util.concurrent.locks.ReentrantLock;

public class TestClass {

	private int m;

	public int inc() {
		return m + 1;
	}

	public static void main(String[] args) {
		String string = "1,2";
		String[] ss = string.split(",");
		System.out.println(ss);
		ReentrantLock reentrantLock = new ReentrantLock();


	}

}
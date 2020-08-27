package juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

class Account {
	private volatile int money;
	private static final AtomicIntegerFieldUpdater<Account> updater = AtomicIntegerFieldUpdater.newUpdater(Account.class, "money");

	Account(int initial) {
		this.money = initial;
	}

	public void increMoney() {
		updater.incrementAndGet(this);
	}

	public int getMoney() {
		return money;
	}

	public String toString() {
		return "Account{" +
				"money=" + money +
				"}";
	}

}

public class FieldUpdaterTest {

	public static void main(String[] args) throws InterruptedException {
		Account account = new Account(0);
		List<Thread> list = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Thread thread = new Thread(new Task(account));
			list.add(thread);
			thread.start();
		}

		for (Thread t : list) {
			t.join();
		}

		System.out.println(account);
	}


	private static class Task implements Runnable {
		private Account account;

		Task(Account account) {
			this.account = account;
		}


		@Override
		public void run() {
			account.increMoney();
		}
	}

}

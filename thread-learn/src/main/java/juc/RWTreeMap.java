package juc;

import basic.Data;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RWTreeMap {
	private final Map<String, Data> m = new TreeMap<>();
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock r = rwl.readLock();
	private final Lock w = rwl.writeLock();

	public Data get(String key) {
		r.lock();
		try {
			return m.get(key);
		} finally {
			r.unlock();
		}
	}

	public String[] allKeys() {
		r.lock();
		try {
			return (String[]) m.keySet().toArray();
		} finally {
			r.unlock();
		}
	}

	public Data put(String key, Data value) {
		w.lock();
		try {
			return m.put(key, value);
		} finally {
			w.unlock();
		}
	}

	public void clear() {
		w.lock();
		try {
			m.clear();
		} finally {
			w.unlock();
		}
	}


}

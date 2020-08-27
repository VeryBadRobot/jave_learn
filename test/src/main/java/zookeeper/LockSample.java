package zookeeper;

import com.sun.scenario.effect.LockableResource;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class LockSample {
	private ZooKeeper zkClient;

	private static final String LOCK_ROOT_PATH = "/Locks";

	private static final String LOCK_NODE_NAME = "Lock_";

	private String lockPath;

	private Watcher watcher = new Watcher() {
		@Override
		public void process(WatchedEvent event) {
			System.out.println(event.getPath() + "前锁释放");
			synchronized (this)
			{
				notifyAll();
			}
		}
	};

	public LockSample() throws IOException {
		zkClient = new ZooKeeper("localhost:2181", 10000, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				if (event.getState() == Event.KeeperState.Disconnected) {
					System.out.println("失去连接");
				}
			}
		});
	}


	public void acquireLock() throws InterruptedException, KeeperException {
		// 创建锁节点
		createLock();

		//尝试获取锁
		attemptLock();
	}


	private void createLock() throws KeeperException, InterruptedException {
		//如果根节点不存在，则创建根节点
		Stat stat = zkClient.exists(LOCK_ROOT_PATH, false);
		if (stat == null) {
			zkClient.create(LOCK_ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}

		// 创建EPHEMERAL_SEQUENTIAL类型节点
		String lockPath = zkClient.create(LOCK_ROOT_PATH + "/" + LOCK_NODE_NAME, Thread.currentThread().getName().getBytes(),
				ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

		System.out.println(Thread.currentThread().getName() + " 锁创建： " + lockPath);
		this.lockPath = lockPath;
	}

	private void attemptLock() throws KeeperException, InterruptedException {
		// 获取Lock所有子节点，按照节点序号排序
		List<String> lockPaths = null;

		lockPaths = zkClient.getChildren(LOCK_ROOT_PATH,false);

		Collections.sort(lockPaths);

		int index = lockPaths.indexOf(lockPath.substring(LOCK_ROOT_PATH.length()+1));

		// 如果lockPath是序号最小的节点，则获取锁
		if (index == 0)
		{
			System.out.println(Thread.currentThread().getName() + " 锁获得，lockPath： " + lockPath);
			return;
		} else {
			// lockPath不是序号最小的节点，监听前一个节点
			String preLockPath = lockPaths.get(index - 1);

			Stat stat = zkClient.exists(LOCK_ROOT_PATH + "/" + preLockPath, watcher);

			if (stat == null)
			{
				attemptLock();
			} else {
				//阻塞当前进程，直到preLockPath释放锁，被watcher观察到，notifyAll后，重新acquireLock
				System.out.println(" 等待前锁释放，prelockPath: " + preLockPath);

				synchronized (watcher)
				{
					watcher.wait();
				}
				attemptLock();
			}

		}
	}

	public void releaseLock() throws KeeperException,InterruptedException{
		zkClient.delete(lockPath,-1);
		zkClient.close();
		System.out.println("锁释放：" + lockPath);
	}

}

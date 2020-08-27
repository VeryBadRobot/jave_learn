package fastdfs;

import cn.strong.fastdfs.client.FastdfsTemplate;
import cn.strong.fastdfs.client.Settings;
import cn.strong.fastdfs.core.FastdfsClient;
import cn.strong.fastdfs.core.SimpleFastdfsClient;
import cn.strong.fastdfs.ex.FastdfsTimeoutException;
import cn.strong.fastdfs.model.Metadata;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.utils.TrackerAddress;
import cn.strong.leke.fs.media.context.TransContext;
import org.apache.poi.hpsf.GUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sun.rmi.runtime.Log;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class FastdfsConfig {

	private int eventLoopThreads = 0;
	private int maxConnPerHost = 10;
	private int timeout = 10000;
	private int maxIdleSeconds = 300;
	private String charset = "UTF-8";

	private String trackers = "127.0.0.1:33000";

	private SimpleFastdfsClient simpleFastdfsClient;

	private FastdfsClient fastdfsClient;


	public static final Logger logger = LoggerFactory.getLogger(FastdfsConfig.class);


	public FastdfsConfig() {
		fastdfsClient = fastdfsClient();
		simpleFastdfsClient = new SimpleFastdfsClient(fastdfsClient);
	}


	public FastdfsTemplate fastdfsTemplate() {
		Settings settings = new Settings();
		settings.setEventLoopThreads(eventLoopThreads);
		settings.setMaxConnPerHost(maxConnPerHost);
		settings.setTimeout(timeout);
		settings.setMaxIdleSeconds(maxIdleSeconds);
		settings.setCharset(charset);
		return new FastdfsTemplate(settings);
	}

	public FastdfsClient fastdfsClient() {
		return new FastdfsClient(fastdfsTemplate(), TrackerAddress.createSeed(trackers));
	}

	public static void main(String[] args) throws InterruptedException {
		FastdfsConfig fastdfsConfig = new FastdfsConfig();
		String path = "F:\\FFOutput\\元曲.mp4";
		logger.info("start");
		fastdfsConfig.testUpload(path);


//		Thread.sleep(2000);
//		System.err.println("over");
	}

	public void testUpload(String path) {
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					String threadName = Thread.currentThread().getName();
					for (int i = 0; i < 1000; i++) {
						long start = System.currentTimeMillis();
						String guid = UUID.randomUUID().toString();
						logger.info("threadName " + threadName + " guid " + guid);
						String storagePath = uploadWorkFile(path, guid);
//					System.out.println("storagePath : " + storagePath);
						deleteFile(storagePath);
						long cost = System.currentTimeMillis() - start;
//					System.out.println("upload load cost " + cost);
					}
				} catch (Exception ex) {
					System.out.println("thread " + Thread.currentThread().getName() + " " + ex);
				}


				System.err.println("over! " + Thread.currentThread().getName());
			}
		};

		executorService.execute(runnable);
		executorService.execute(runnable);
	}




	/**
	 * 上传工作目录文件
	 *
	 * @param fileName
	 * @return
	 */
	private String uploadWorkFile(String fileName, String name) throws InterruptedException, ExecutionException, TimeoutException {
		File file = new File(fileName);


		CompletableFuture<String> retry = this.uploadAndRetry(() -> fastdfsClient.upload(file, null, name), name);

		return retry.join();
	}

	private void deleteFile(String path) {
		//System.out.println("delete file: " + path);
		simpleFastdfsClient.delete(path);
	}

//	/**
//	 * 上传文件，如果失败重试一次，再次失败返回空值。
//	 *
//	 * @param func 上传处理逻辑
//	 * @return
//	 */
//	private CompletableFuture<String> uploadAndRetry(Supplier<CompletableFuture<StoragePath>> func) {
//		CompletableFuture<StoragePath> a = func.get();
//		CompletableFuture<String> b = a.thenApplyAsync(v -> {
//			//System.out.println("First");
//			return v.toString();
//		}).exceptionally(ex -> {
//
//			System.err.println("File upload failed, retry again. " + Thread.currentThread().getName() +" | "+ ex.hashCode());
//			try {
//				CompletableFuture<String> c= a.thenApplyAsync(v -> {
//					System.out.println("Second: " + Thread.currentThread().getName()+v.toString());
//					return v.toString();
//				}).whenComplete((data, t)->{
//					System.out.println("Second Complete "+ Thread.currentThread().getName() +" | "+ t.hashCode());
//				});
//				return c.get();
//			} catch (InterruptedException | ExecutionException e) {
//				System.err.println("File again upload failed, give it up." + ex);
//				return null;
//			}
//		});
//
//		return b;
//	}


	/**
	 * 上传文件，如果失败重试一次，再次失败返回空值。
	 *
	 * @param func 上传处理逻辑
	 * @return
	 */
	private CompletableFuture<String> uploadAndRetry(Supplier<CompletableFuture<StoragePath>> func, String guid) {
		return func.get().thenApply(v -> {
			logger.info("First success " + Thread.currentThread().getName() + " guid : " + guid);
			return v.toString();
		}).exceptionally(ex -> {
			ThreadLocal local = new ThreadLocal();
			logger.info("File upload failed, retry again. " + Thread.currentThread().getName() + "guid : " + guid + " local: " + local.get() + " " + ex);
			try {
				return func.get().thenApply(v -> {
					logger.info("Second success " + Thread.currentThread().getName() + " guid : " + guid);
					return v.toString();
				}).get();
			} catch (InterruptedException | ExecutionException e) {
				logger.info("File again upload failed, give it up." + Thread.currentThread().getName() + " guid : " + guid + " ex "+ex);

				return null;
			}
		});
	}

	public void testgetMetaData() {
		ExecutorService executorService = Executors.newFixedThreadPool(600);
		String path = "group1/M00/F8/2A/wKgURF6iy2OAcq1OAAAmAJUxTxc738.doc";
		executorService.execute(new Runnable() {
			@Override
			public void run() {

				for (int i = 0; i < 1000; i++) {
					long start = System.currentTimeMillis();
					byte[] bytes = simpleFastdfsClient.download(path);

					long cost = System.currentTimeMillis() - start;
					//Metadata metadata = simpleFastdfsClient.getMetadata(path);
					System.out.println("down load cost " + cost);

//					System.out.println(metadata);
				}
			}
		});
	}


}

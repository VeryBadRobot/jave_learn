package cn.strong.leke.fs.media.boot;

import cn.strong.fastdfs.core.FastdfsClient;
import cn.strong.fastdfs.core.SimpleFastdfsClient;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.sink.OutputStreamSink;
import cn.strong.leke.fs.media.config.NodeSettings;
import cn.strong.leke.fs.media.service.MessageSender;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

@Configuration
@ComponentScan("cn.strong.leke.fs.media.config")
public class MediaTransApplication {

	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		System.out.println("MediaTransNode starting.");
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				MediaTransApplication.class)) {
			NodeSettings settings = context.getBean(NodeSettings.class);

			StringBuilder builder = new StringBuilder();
			builder.append("Node Settings: ");
			builder.append("\n  ").append("id: ").append(settings.getNodeId());
			builder.append("\n  ").append("ip: ").append(settings.getAddress());
			builder.append("\n  ").append("cores: ").append(settings.getCores());
			builder.append("\n  ").append("works: ").append(settings.getWorks());
			builder.append("\n  ").append("swap: ").append(settings.getSwapDir());
			System.out.println(builder);

			time = System.currentTimeMillis() - time;
			System.out.println("MediaTransNode started, use " + time + "ms");

			//主线程沉睡，其他线程启动
			for (int i = 1; i < Integer.MAX_VALUE; i++) {
				try {

					test(context);
					Thread.sleep(TimeUnit.DAYS.toMillis(1));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("MediaTransNode has been running " + i + " days.");
			}
		}
	}


	public static void download(AnnotationConfigApplicationContext context) throws FileNotFoundException {
		String path = "";
		FastdfsClient fastdfsClient = context.getBean(FastdfsClient.class);

		SimpleFastdfsClient simpleFastdfsClient = context.getBean(SimpleFastdfsClient.class);
		OutputStream outputStream =new FileOutputStream("test.mp4");
		try (OutputStreamSink sink = new OutputStreamSink(outputStream)) {

				//下载单个文件
				fastdfsClient.download(StoragePath.valueOf(path), sink, null).get();

		} catch (Exception e) {
			System.out.println("文件下载失败");
		}


	}


	public static void test(AnnotationConfigApplicationContext context) {
		try {
			Thread.sleep(5000);

			MessageSender messageSender = context.getBean(MessageSender.class);
			for (int i = 0; i < 1; i++) {
				messageSender.sendMediaTask();
			}


		} catch (Exception e) {

		}

	}


}
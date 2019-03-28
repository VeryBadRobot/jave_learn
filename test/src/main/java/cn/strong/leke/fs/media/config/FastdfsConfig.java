package cn.strong.leke.fs.media.config;

import cn.strong.fastdfs.client.FastdfsTemplate;
import cn.strong.fastdfs.client.Settings;
import cn.strong.fastdfs.core.FastdfsClient;
import cn.strong.fastdfs.core.SimpleFastdfsClient;
import cn.strong.fastdfs.utils.TrackerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FastdfsConfig {

	@Value("${fastdfs.eventLoopThreads}")
	private int eventLoopThreads;
	@Value("${fastdfs.maxConnPerHost}")
	private int maxConnPerHost;
	@Value("${fastdfs.timeout}")
	private int timeout;
	@Value("${fastdfs.maxIdleSeconds}")
	private int maxIdleSeconds;
	@Value("${fastdfs.charset}")
	private String charset = "UTF-8";
	@Value("${fastdfs.trackers}")
	private String trackers;

	@Bean
	public FastdfsTemplate fastdfsTemplate() {
		Settings settings = new Settings();
		settings.setEventLoopThreads(eventLoopThreads);
		settings.setMaxConnPerHost(maxConnPerHost);
		settings.setTimeout(timeout);
		settings.setMaxIdleSeconds(maxIdleSeconds);
		settings.setCharset(charset);
		return new FastdfsTemplate(settings);
	}

	@Bean
	public FastdfsClient fastdfsClient() {
		return new FastdfsClient(fastdfsTemplate(), TrackerAddress.createSeed(trackers));
	}

	@Bean
	public SimpleFastdfsClient simpleFastdfsClient() {
		return new SimpleFastdfsClient(fastdfsClient());
	}
}

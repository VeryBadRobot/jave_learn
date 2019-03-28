package cn.strong.leke.fs.media.config;

import cn.strong.leke.fs.media.service.MessageSender;
import cn.strong.leke.fs.media.service.StorageService;
import cn.strong.leke.fs.media.service.TranscodeService;
import cn.strong.leke.fs.media.service.TransformService;
import cn.strong.leke.fs.media.util.NetworkUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.File;

@Configuration
@PropertySource("classpath:/application.properties")
public class ServiceConfig {

	@Bean
	public NodeSettings nodeSettings() {
		NodeSettings settings = new NodeSettings();

		// 节点信息
		String ip = NetworkUtils.getIpAddress();
		settings.setNodeId(ip);
		settings.setAddress(ip);

		// 逻辑线程总数
		int cores = Runtime.getRuntime().availableProcessors();
		//设置节点线程数，为啥是cpu核数/4?
		int works = Math.max(1, Math.min(cores / 4, 4));
		settings.setCores(cores);
		settings.setWorks(works);

		// 交换目标
		// 当前项目根路径
		File swapDir = new File(System.getProperty("user.dir"), "swap");
		settings.setSwapDir(swapDir);

		return settings;
	}



	/**
	 * 消息发送服务
	 *
	 * @return
	 */
	@Bean
	public MessageSender messageSender() {
		return new MessageSender();
	}

	/**
	 * 存储服务
	 *
	 * @return
	 */
	@Bean
	public StorageService storageService() {
		return new StorageService();
	}

	/**
	 * 转码服务
	 *
	 * @return
	 */
	@Bean
	public TranscodeService transcodeService() {
		return new TranscodeService();
	}

	/**
	 * 转换服务
	 *
	 * @return
	 */
	@Bean
	public TransformService transformService() {
		return new TransformService();
	}
}

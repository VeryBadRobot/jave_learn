package cn.strong.leke.fs.media.config;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitConfig {

	/**
	 * 主机IP
	 */
	@Value("${mq.filetrans.host}")
	private String host;
	/**
	 * 端口号
	 */
	@Value("${mq.filetrans.port}")
	private int port;
	/**
	 * 用户名
	 */
	@Value("${mq.filetrans.username}")
	private String username;
	/**
	 * 密码
	 */
	@Value("${mq.filetrans.password}")
	private String password;

	@Bean
	public ConnectionFactory connectionFactory() {
		//只缓存一个session
		CachingConnectionFactory result = new CachingConnectionFactory();
		result.setHost("localhost");
//		result.setHost(host);
//		result.setPort(port);
//		result.setUsername(username);
//		result.setPassword(password);
		return result;
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		return new RabbitTemplate(connectionFactory);
	}

	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}


}

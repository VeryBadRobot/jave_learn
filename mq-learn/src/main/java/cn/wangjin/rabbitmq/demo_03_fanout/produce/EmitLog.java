package cn.wangjin.rabbitmq.demo_03_fanout.produce;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EmitLog {
	public static final String EXCHANGE_NAME = "logs";

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");

		Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();

		/**
		 * fanout表示分发，所有消费者得到同样的消息
		 */
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

		//分发消息
		for (int i = 0; i < 5; i++) {
			String message = "Hello word " + i;
			channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
			System.out.println("EmitLog send '" + message + "'");
		}


		channel.close();
		connection.close();

	}
}

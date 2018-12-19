package cn.wangjin.rabbitmq.demo_02.produce;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class NewTask {
	private static final String TASK_QUEUE_NAME = "task_queue";

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();

		factory.setHost("localhost");

		Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();

		 AMQP.Queue.DeclareOk ok = channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

		//分发消息
		for (int i = 0; i < 10; i++) {
			String message = "Hello RabbitMQ" + i;
			channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
			System.out.println("NewTask send '" + message + "'");
		}


		System.out.println(ok.getConsumerCount());
		System.out.println(ok.getMessageCount());
		channel.close();
		connection.close();

	}


}

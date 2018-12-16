package cn.wangjin.rabbitmq.demo_03_fanout.consume;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogs1 {
	private static final String EXCHANGE_NAME = "logs";

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

		//产生一个随机队列的名称
		String queueName = channel.queueDeclare().getQueue();

		//绑定队列和交换机
		channel.queueBind(queueName, EXCHANGE_NAME, "");

		System.out.println("ReceiveLogs1 waiting for message ");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body);
				System.out.println("ReceiveLogs1 received '" + message + "'");
			}
		};
		//队列会自动删除
		channel.basicConsume(queueName, true, consumer);
	}

}

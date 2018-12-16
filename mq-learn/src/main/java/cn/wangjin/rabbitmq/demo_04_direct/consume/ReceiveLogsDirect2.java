package cn.wangjin.rabbitmq.demo_04_direct.consume;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsDirect2 {
	// 交换机名称
	private static final String EXCHANGE_NAME = "direct_logs";
	// 路由关键字
	private static final String[] routingKeys = new String[]{"info", "warning"};

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "direct");

		String queueName = channel.queueDeclare().getQueue();

		for (String routingKey : routingKeys) {
			channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
			System.out.println("ReceiveLogDirect1 exchange " + EXCHANGE_NAME + ", " + " queue " + queueName
					+ ", BindRoutingKey: " + routingKey);
		}

		System.out.println("ReceiveLogsDirect1 waiting for message");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body);
				System.out.println("ReceiveLogsDirect1 Received :'" + envelope.getRoutingKey() + "', delivery tag:'" +
						envelope.getDeliveryTag() + "', message :'" + message + "'");
			}
		};

		channel.basicConsume(queueName, true, consumer);
	}
}

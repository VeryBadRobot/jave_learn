package cn.wangjin.rabbitmq.demo_05_topic.consume;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * * ：可以替代一个词
 * #：可以替代0或者更多的词
 */
public class ReceiveLogsTopic1 {
	private static final String EXCHANGE_NAME = "topic_logs";


	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();

		factory.setHost("localhost");

		Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();

		//声明一个匹配模式的交换机
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");

		String queueName = channel.queueDeclare().getQueue();

		//路由关键字
		String[] routingKeys = new String[]{"*.orange.*"};
		//绑定路由关键字
		for (String routingKey : routingKeys) {
			channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
			System.out.println("ReceiveLogsTopic1 exchange: " + EXCHANGE_NAME + ", queue: " + queueName + ", BindRoutingKey: " + routingKey);
		}

		System.out.println("ReceiveLogsTopic1 waiting for messages");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body);
				System.out.println("ReceiveLogsTopic1 received routingKey:'" + envelope.getRoutingKey() + "', message:'" + message + "'");
			}
		};

		channel.basicConsume(queueName, true, consumer);
	}

}

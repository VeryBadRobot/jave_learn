package cn.wangjin.rabbitmq.demo_02.consume;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Worker2 {
	public static final String TASK_QUEUE_NAME = "task_queue";


	public static void main(String[] args) throws IOException, TimeoutException {
		final ConnectionFactory factory = new ConnectionFactory();

		factory.setHost("localhost");

		Connection connection = factory.newConnection();

		final Channel channel = connection.createChannel();

		channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

		System.err.println("Worker1 waiting for messages");

		/**
		 * 每次从队列中获取的数量
		 */
		channel.basicQos(1);

		final Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.err.println("Worker1 received '" + message + "'");

				try {
					//throw new Exception();
					doWork(message);
				} catch (Exception e) {
					channel.abort();
				} finally {
					System.err.println("Worker1 Done");
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};


		boolean autoAck = false;
		//消息消费完成确认
		channel.basicConsume(TASK_QUEUE_NAME, autoAck, consumer);
	}


	private static void doWork(String task) {
		try {
			//暂停1秒
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

}

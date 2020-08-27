package cn.wangjin.rabbitmq.demo_01.consume;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Customer {

	/**
	 * 队列名称
	 */
	public static final String QUEUE_NAME = "rabbitmq.test";

	public static void main(String[] args) throws IOException, TimeoutException {
		/**
		 * 设置RabbitMQ连接信息
		 */
		//创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		//设置rabbitmq server 地址
		factory.setHost("192.168.20.19");
		//设置用户名
		factory.setUsername("leke");
		//设置密码
		factory.setPassword("leke@@!");
		//设置端口号
		factory.setPort(5672);

		//创建一个连接
		Connection connection = factory.newConnection();
		//创建一个通道
		Channel channel = connection.createChannel();

		/**
		 *  不声明也可读到消息？如果声明了则必须与生产者声明的消息队列属性一致
		 */

		//声明要关注的队列
		/**
		 * queueDeclare第一个参数表示队列名称、
		 * 第二个参数为是否持久化（true表示是，队列将在服务器重启时生存）、
		 * 第三个参数为是否是独占队列（创建者可以使用的私有队列，断开后自动删除）、
		 * 第四个参数为当所有消费者客户端连接断开时是否自动删除队列、
		 * 第五个参数为队列的其他参数
		 */
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		System.err.println("Customer Waiting Received messages");

		/**
		 *  DefaultConsumer类实现了Consumer接口，通过传入一个频道，
		 *  告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
		 */
		Consumer consumer = new DefaultConsumer(channel) {

			/**
			 * envelope主要存放生产者相关信息（比如交换机、路由key等）body是消息实体。
			 * @param consumerTag
			 * @param envelope
			 * @param properties
			 * @param body
			 * @throws IOException
			 */
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.err.println("Customer Received '" + message + "'");
			}
		};

		/**
		 * 自动回复队列应答 -- RabbitMQ中的消息确认机制
		 */
		channel.basicConsume(QUEUE_NAME, true, consumer);

		/**
		 * 未关闭通道和连接，才可以持续应答
		 */
	}


}

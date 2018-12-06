package cn.wangjin.rabbitmq.demo_01.produce;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

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

		//设置rabbitmq server地址
		factory.setHost("localhost");
		//设置用户名
//		factory.setUsername();
		//设置密码
//		factory.setPassword();
		//设置端口号
//		factory.setPort();

		//创建一个连接
		Connection connection = factory.newConnection();
		//创建一个通道
		Channel channel = connection.createChannel();

		/**
		 * queueDeclare第一个参数表示队列名称、
		 * 第二个参数为是否持久化（true表示是，队列将在服务器重启时生存）、
		 * 第三个参数为是否是独占队列（创建者可以使用的私有队列，断开后自动删除）、
		 * 第四个参数为当所有消费者客户端连接断开时是否自动删除队列、
		 * 第五个参数为队列的其他参数
		 */
		//声明一个队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		//声明消息
		String message = "Hello RabbitMQ!";

		/**
		 * basicPublish第一个参数为交换机名称、
		 * 第二个参数为队列映射的路由key、
		 * 第三个参数为消息的其他属性、
		 * 第四个参数为发送信息的主体
		 */
		//发送消息到队列
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

		System.err.println("Producer Send + '" + message + "'");

		//关闭通道
		channel.close();
		//关闭连接
		connection.close();


	}

}

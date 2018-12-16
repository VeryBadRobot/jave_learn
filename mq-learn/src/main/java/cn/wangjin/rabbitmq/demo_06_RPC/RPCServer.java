package cn.wangjin.rabbitmq.demo_06_RPC;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RPC服务器
 * <p>
 * 1：建立连接，通道，队列
 * <p>
 * 2：我们可能运行多个服务器进程，为了分散负载服务器压力，我们设置channel.basicQos(1);
 * <p>
 * 3：我们用basicconsume访问队列。然后进入循环，等待请求消息并处理消息然后发送响应。
 */
public class RPCServer {
	private static final String RPC_QUEUE_NAME = "rpc_queue";

	private static int fib(int n) {
		if (n == 0) {
			return 0;
		}
		if (n == 1) {
			return 1;
		}

		return fib(n - 1) + fib(n - 1);
	}


	public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");

		Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();

		channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
		channel.basicQos(1);

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(RPC_QUEUE_NAME, false, consumer);

		System.out.println("RPCServer awating RPC request");
		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			//读取消息属性
			BasicProperties props = delivery.getProperties();

			BasicProperties replyProps = new AMQP.BasicProperties.Builder().correlationId(props.getCorrelationId()).build();

			//消息体
			String message = new String(delivery.getBody(), "utf-8");

			int n = Integer.parseInt(message);

			System.out.println("RPCServer fib(" + message + ")");

			String response = "" + fib(n);

			//发送队列
			channel.basicPublish("", props.getReplyTo(), (AMQP.BasicProperties) replyProps, response.getBytes());
			//确认消息
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		}


	}

}

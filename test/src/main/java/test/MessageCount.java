package test;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MessageCount {


	private String host = "192.168.20.75";


	private Integer port = 5672;


	private String username = "leke";


	private String password = "leke@@@";


	public void printMessageCount() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
//设置ip，port，用户名和秘密
		factory.setHost(host);
		factory.setPort(port);
		factory.setUsername(username);
		factory.setPassword(password);

		//创建链接
		Connection connection = factory.newConnection();

		//创建信道
		Channel channel = connection.createChannel();

//		//创建一个type=direct 持久化的 非自动删除的交换器
//		channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);

		String queName = "q.filetrans.doc_task_gs";
		AMQP.Queue.DeclareOk declareOk = channel.queueDeclarePassive(queName);
		//获取队列中的消息个数
		int num = declareOk.getMessageCount();

		System.out.println("messageCount: " + num);
		System.out.println("consumerCount: "+ declareOk.getConsumerCount() );

	}

	public static void main(String[] args) throws IOException, TimeoutException {
		MessageCount messageCount = new MessageCount();
		messageCount.printMessageCount();
	}


}

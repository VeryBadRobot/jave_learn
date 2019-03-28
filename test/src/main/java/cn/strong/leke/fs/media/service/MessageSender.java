package cn.strong.leke.fs.media.service;

import cn.strong.leke.fs.media.model.*;
import cn.strong.leke.fs.media.util.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 消息发送。
 */
@Component
public class MessageSender {

	private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

	@Value("${mq.e.filetrans.nodebeat}")
	private String nodebeatExchange;
	@Value("${mq.q.filetrans.nodebeat}")
	private String nodebeatQueueName;

	@Value("${mq.e.filetrans.errorlog}")
	private String errorlogExchange;
	@Value("${mq.q.filetrans.errorlog}")
	private String errorlogQueueName;

	@Value("${mq.e.filetrans.media_task_feedback}")
	private String feedbackExchange;
	@Value("${mq.q.filetrans.media_task_feedback}")
	private String feedbackQueueName;

	@Value("${mq.e.filetrans.media_task_progress}")
	private String progressExchange;
	@Value("${mq.q.filetrans.media_task_progress}")
	private String progressQueueName;


	@Value("${mq.e.filetrans.media_task}")
	private String mediaTaskExchange;
	@Value("${mq.q.filetrans.media_task}")
	private String mediaTaskQueueName;

	@Resource
	private RabbitTemplate rabbitTemplate;


	/**
	 * 切片交换机
	 */
	private String docSplitExchange = "filetrans.doc_split_task";

	/**
	 * 切片队列
	 */
	private String docSplitQueueName = "filetrans.doc_split_exchange";


	public static int count = 0;

	/**
	 * 发送节点心跳消息
	 *
	 * @param nodeBeatMsg
	 */
	public void sendNodeBeatMsg(NodeBeatMsg nodeBeatMsg) {
		String message = JSON.stringify(nodeBeatMsg);
		this.send(this.nodebeatExchange, this.nodebeatQueueName, message);
	}

	/**
	 * 发送错误日志消息
	 *
	 * @param errorLogMsg
	 */
	public void sendErrorLogMsg(ErrorLogMsg errorLogMsg) {
		String message = JSON.stringify(errorLogMsg);
		this.send(this.errorlogExchange, this.errorlogQueueName, message);
	}

	/**
	 * 发送进度消息
	 *
	 * @param progressMsg
	 */
	public void sendProgressMsg(ProgressMsg progressMsg) {
		String message = JSON.stringify(progressMsg);
		this.send(this.progressExchange, this.progressQueueName, message);
	}

	/**
	 * 发送转换响应消息
	 *
	 * @param transRespMsg
	 */
	public void sendTransRespMsg(TransRespMsg transRespMsg) {
		String message = JSON.stringify(transRespMsg);
		//转换成功直接发送，转换失败设置错误日志为"略"
		if (transRespMsg.getSuccess()) {
			logger.info("File {} Sender: {}.", transRespMsg.getFileId(), message);
		} else {
			transRespMsg.setErrLog("略");
			logger.info("File {} Sender: {}.", transRespMsg.getFileId(), JSON.stringify(transRespMsg));
		}
		this.send(this.feedbackExchange, this.feedbackQueueName, message);
	}

	/**
	 * 发送消息
	 *
	 * @param exchange
	 * @param queue
	 * @param message
	 */
	private void send(String exchange, String queue, String message) {
		//以UTF-8格式转码消息
		byte[] body = message.getBytes(StandardCharsets.UTF_8);
		this.rabbitTemplate.convertAndSend(exchange, queue, body);

	}


	/**
	 * 分发媒体转换任务
	 */
	public void sendMediaTask() {

		System.out.println("------------------------start-------------------------");
		TransReqMsg transReqMsg = new TransReqMsg();
		transReqMsg.setFileId("test : " + count);
		transReqMsg.setType("" + count);
		transReqMsg.setNodeId("192.168.163.1");
		transReqMsg.setMaxRetryCount(10);
		transReqMsg.setMessageId(UUID.randomUUID().toString());
		String string = JSON.stringify(transReqMsg);
		logger.info("Send media task: {}.", string);
		//将消息转换为字节数组
		byte[] body = string.getBytes(StandardCharsets.UTF_8);
		//消息队列发送转换消息
		this.rabbitTemplate.convertAndSend(this.mediaTaskExchange, this.mediaTaskQueueName, body, (msg) -> {
			msg.getMessageProperties().setPriority(5);
			msg.getMessageProperties().getHeaders().put("retry_count", "1");
			return msg;
		});
		count++;
		System.out.println("send message : " + string);
	}

}

package cn.strong.leke.fs.media.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class QueueConfig {

	/**
	 * 心跳队列交换机
	 */
	@Value("${mq.e.filetrans.nodebeat}")
	private String nodebeatExchange;
	/**
	 * 节点心跳队列名称
	 */
	@Value("${mq.q.filetrans.nodebeat}")
	private String nodebeatQueueName;

	/**
	 * 错误日志交换机
	 */
	@Value("${mq.e.filetrans.errorlog}")
	private String errorlogExchange;
	/**
	 * 错误日志队列名
	 */
	@Value("${mq.q.filetrans.errorlog}")
	private String errorlogQueueName;

	/**
	 * 媒体转换任务交换机
	 */
	@Value("${mq.e.filetrans.media_task}")
	private String mediaTaskExchange;
	/**
	 * 媒体转换任务队列名
	 */
	@Value("${mq.q.filetrans.media_task}")
	private String mediaTaskQueueName;

	/**
	 * 媒体转换任务反馈交换机
	 */
	@Value("${mq.e.filetrans.media_task_feedback}")
	private String mediaTaskFeedbackExchange;
	/**
	 * 媒体转换任务反馈队列名
	 */
	@Value("${mq.q.filetrans.media_task_feedback}")
	private String mediaTaskFeedbackQueueName;

	/**
	 * 媒体转换任务进度交换机
	 */
	@Value("${mq.e.filetrans.media_task_progress}")
	private String mediaTaskProgressExchange;
	/**
	 * 媒体转换任务进度队列名
	 */
	@Value("${mq.q.filetrans.media_task_progress}")
	private String mediaTaskProgressQueueName;



	/**
	 * 切片交换机
	 */
	private String docSplitExchange = "filetrans.doc_split_task";

	/**
	 * 切片队列
	 */
	private String docSplitQueueName = "filetrans.doc_split_exchange";

	@Resource
	private RabbitAdmin rabbitAdmin;

	@PostConstruct
	public void init() {
		this.declare(this.nodebeatQueueName, this.nodebeatExchange);
		this.declare(this.errorlogQueueName, this.errorlogExchange);
		this.declare(this.mediaTaskQueueName, this.mediaTaskExchange, 10);
		this.declare(this.mediaTaskFeedbackQueueName, this.mediaTaskFeedbackExchange);
		this.declare(this.mediaTaskProgressQueueName, this.mediaTaskProgressExchange);

		this.declare(this.docSplitQueueName,this.docSplitExchange,10);
	}

	/**
	 * 声明普通队列
	 *
	 * @param queue
	 * @param exchange
	 */
	private void declare(String queue, String exchange) {
		Queue q = new Queue(queue);
		//声明队列
		rabbitAdmin.declareQueue(q);
		DirectExchange e = new DirectExchange(exchange);
		//声明交换机
		rabbitAdmin.declareExchange(e);
		//使用队列名绑定队列和交换机
		rabbitAdmin.declareBinding(BindingBuilder.bind(q).to(e).with(queue));
	}

	/**
	 * 声明优先级队列
	 *
	 * @param queue
	 * @param exchange
	 * @param priority
	 */
	private void declare(String queue, String exchange, int priority) {
		Map<String, Object> arguments = new HashMap<>();
		arguments.put("x-max-priority", priority);
		Queue q = new Queue(queue, true, false, false, arguments);
		rabbitAdmin.declareQueue(q);
		DirectExchange e = new DirectExchange(exchange);
		rabbitAdmin.declareExchange(e);
		rabbitAdmin.declareBinding(BindingBuilder.bind(q).to(e).with(queue));
	}
}

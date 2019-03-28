package cn.strong.leke.fs.media.service;

import cn.strong.leke.fs.media.config.NodeSettings;
import cn.strong.leke.fs.media.context.FileInfo;
import cn.strong.leke.fs.media.context.MediaInfo;
import cn.strong.leke.fs.media.context.TransContext;
import cn.strong.leke.fs.media.context.TransError;
import cn.strong.leke.fs.media.model.*;
import cn.strong.leke.fs.media.util.JSON;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 转换服务。
 */
public class TransformService {

	private static final Logger logger = LoggerFactory.getLogger(TransformService.class);

	/**
	 * 心跳次数
	 */
	private int beatNo = 0;

	/**
	 * 节点设置
	 */
	@Resource
	private NodeSettings settings;

	/**
	 * 消息发送
	 */
	@Resource
	private MessageSender messageSender;
	/**
	 * 存储服务
	 */
	@Resource
	private StorageService storageService;
	/**
	 * 转码服务
	 */
	@Resource
	private TranscodeService transcodeService;

	/**
	 * 转换服务启动的时候初始化文件交换目录
	 *
	 * @throws Exception
	 */
	@PostConstruct
	public void startup() throws Exception {
		this.initSwapDirectory(this.settings.getSwapDir());
	}

	/**
	 * 初始化文件交换目录，如果初始化失败，抛出异常
	 *
	 * @throws Exception
	 */
	private void initSwapDirectory(File file) throws Exception {
		try {
			if (file.exists()) {
				FileUtils.cleanDirectory(file);
			} else {
				FileUtils.forceMkdir(file);
			}
		} catch (IOException e) {
			logger.error("无法创建文件交换目录，请确认您的文件读写权限", e);
			throw e;
		}
	}

	/**
	 * 初始化线程工作目录，如果处理失败抛出运行时异常。
	 *
	 * @return
	 */
	private File initWorkDirectory() {
		String name = Thread.currentThread().getName();
		File file = new File(this.settings.getSwapDir(), name);
		try {
			if (file.exists()) {
				//清理目录内容
				FileUtils.cleanDirectory(file);
			} else {
				//强制创建目录，会创建父目录
				FileUtils.forceMkdir(file);
			}
		} catch (IOException e) {
			throw new RuntimeException("创建线程工作目录失败", e);
		}
		return file;
	}

	/**
	 * 清空线程工作目录。
	 */
	private void clearWorkDirectory() {
		String name = Thread.currentThread().getName();
		File dir = new File(this.settings.getSwapDir(), name);
		try {
			FileUtils.cleanDirectory(dir);
		} catch (IOException e) {
		}
	}




	/**
	 * 监听转换消息，处理并响应结果
	 *
	 * @param bytes
	 */

	public void doTransform(byte[] bytes) {
		String message = new String(bytes);
		//媒体转换请求消息
		TransReqMsg transReqMsg = JSON.parse(message, TransReqMsg.class);
		//打印日志，接收到文件
		logger.info("File {} Receive: {}.", transReqMsg.getFileId(), message);
		//创建转换任务上下文
		TransContext context = new TransContext();
		//设置接收文件时间
		context.setAcceptOn(new Date());
		//设置文件信息
		context.setFileInfo(transReqMsg.toFileInfo());
		try {
			// 1、接收任务完成初始化，完成后进度估算为：1
			//当前线程名
			String workerId = Thread.currentThread().getName();
			logger.info("File {} trans work is {}.", transReqMsg.getFileId(), workerId);
			//设置工作线程名
			context.setWorkerId(workerId);
			//设置工作目录
			context.setWorkDirectory(this.initWorkDirectory());
			//创建进度指示器
			ProgressHandler handler = new ProgressHandler(context, this);
			//初始化进度为1并发送消息
			handler.onReceived();

			// 2、下载文件，完成后进度估算为：10
			this.storageService.download(context, handler);

			// 3、转换文件，完成后进度估算为：90
			//转换开始前修改进度为10%
			handler.onBeforeTranscode();
			//执行转换业务
			this.transcodeService.execute(context, handler);
			//转换完成后修改进度为90%
			handler.onAfterTranscode();

			// 4、上传文件，完成后进度估算为：99
			this.storageService.upload(context, handler);
			// 修改进度为99%
			handler.onRespond();

			// 5、响应转换结果
			context.setReplyOn(new Date());
			//  发送转换成功消息
			this.sendTransRespMsgAsSuccess(context);

			//写日志记录转换耗时
			long time = context.getReplyOn().getTime() - context.getAcceptOn().getTime();
			logger.info("File {} trans success, used {}ms.", transReqMsg.getFileId(), time);
		} catch (TransformException e) {
			logger.error("File {} transform error.", transReqMsg.getFileId(), e);
			this.sendTransRespMsgAsFailure(context, e);
		} catch (Exception e) {
			logger.error("File {} unknown error.", transReqMsg.getFileId(), e);
			this.sendErrorLogMsg(context, e);
			try {
				// 出现异常停一下，预防死循环
				Thread.sleep(5000);
			} catch (Exception ex) {
				// empty
			}
			throw e;
		} finally {
			//清理工作目录
			this.clearWorkDirectory();
		}
	}

	/**
	 * 发送转换正确消息
	 *
	 * @param context 上下文
	 */
	private void sendTransRespMsgAsSuccess(TransContext context) {
		TransRespMsg msg = new TransRespMsg();
		MediaInfo mediaInfo = context.getMediaInfo();
		//设置文件ID
		msg.setFileId(context.getFileInfo().getFileId());
		//设置转换成功
		msg.setSuccess(true);
		//设置错误码
		msg.setErrCode(null);
		//设置错误信息
		msg.setErrInfo(null);
		//设置错误日志
		msg.setErrLog(null);
		//设置转换后的服务器m3u8文件路径
		msg.setM3u8Path(mediaInfo.getPath());
		//设置音频时长
		msg.setDuration(mediaInfo.getDuration());
		//设置质量
		msg.setQuality(mediaInfo.getQuality());
		//设置节点ID
		msg.setNodeId(this.settings.getNodeId());
		//设置线程名
		msg.setWorkerId(context.getWorkerId());
		//设置接收时间
		msg.setAcceptOn(context.getAcceptOn());
		//设置响应时间
		msg.setReplyOn(context.getReplyOn());
		//发送消息
		this.messageSender.sendTransRespMsg(msg);
	}

	/**
	 * 发送转换错误消息
	 *
	 * @param context 上下文
	 * @param e       错误信息
	 */
	private void sendTransRespMsgAsFailure(TransContext context, TransformException e) {
		logger.info("File {} trans failed.", context.getFileInfo().getFileId(), e);
		String errCode = e.getMessage();
		String errInfo = TransError.getInfo(errCode);
		String errLog = ExceptionUtils.getFullStackTrace(e);

		TransRespMsg msg = new TransRespMsg();
		msg.setFileId(context.getFileInfo().getFileId());
		msg.setSuccess(false);
		msg.setErrCode(Integer.parseInt(errCode));
		msg.setErrInfo(errInfo);
		msg.setErrLog(errLog);
		msg.setNodeId(this.settings.getNodeId());
		msg.setWorkerId(context.getWorkerId());
		msg.setAcceptOn(context.getAcceptOn());
		msg.setReplyOn(context.getReplyOn());
		this.messageSender.sendTransRespMsg(msg);
	}

	/**
	 * 发送进度消息
	 *
	 * @param context  文件ID
	 * @param progress 进度百分比
	 */
	protected void sendProgressMsg(TransContext context, int progress) {
		FileInfo fileInfo = context.getFileInfo();
		ProgressMsg msg = new ProgressMsg();
		msg.setFileId(fileInfo.getFileId());
		msg.setNodeId(this.settings.getNodeId());
		msg.setWorkerId(context.getWorkerId());
		msg.setProgress(progress);
		this.messageSender.sendProgressMsg(msg);
		logger.info("File {} trans progress {}%.", context.getFileInfo().getFileId(), progress);
	}

	/**
	 * 发送转换错误消息
	 *
	 * @param context
	 * @param t
	 */
	private void sendErrorLogMsg(TransContext context, Throwable t) {
		String errCode, errInfo, errLog;
		if (t instanceof TransformException) {
			errCode = t.getMessage();
			errInfo = TransError.getInfo(errCode);
			errLog = ExceptionUtils.getFullStackTrace(t);
		} else {
			errCode = TransError.UNKNOWN.code;
			errInfo = TransError.UNKNOWN.info;
			errLog = ExceptionUtils.getFullStackTrace(t);
		}

		ErrorLogMsg msg = new ErrorLogMsg();
		msg.setFileId(context.getFileInfo().getFileId());
		msg.setPageNo(1);
		msg.setNodeId(this.settings.getNodeId());
		msg.setNodeType(2);
		msg.setWorkerId(context.getWorkerId());
		msg.setErrTime(new Date());
		msg.setErrCode(Integer.parseInt(errCode));
		msg.setErrInfo(errInfo);
		msg.setErrLog(errLog);
		this.messageSender.sendErrorLogMsg(msg);
	}

	/**
	 * 心跳任务，自启动后每30秒执行一次。
	 */
	@Scheduled(initialDelay = 3000, fixedDelay = 30000)
	public void handleNodeBeatMsg() {
		NodeBeatMsg nodeBeatMsg = new NodeBeatMsg();
		nodeBeatMsg.setNodeId(this.settings.getNodeId());
		nodeBeatMsg.setNodeType(2);
		nodeBeatMsg.setWorkNum(this.settings.getWorks());
		nodeBeatMsg.setAddress(this.settings.getAddress());
		nodeBeatMsg.setIsHealth(true);
		nodeBeatMsg.setBeatNo(beatNo);
		nodeBeatMsg.setBeatOn(new Date());
		this.messageSender.sendNodeBeatMsg(nodeBeatMsg);
		beatNo++;
	}
}

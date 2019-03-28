package cn.strong.leke.fs.media.service;

import cn.strong.leke.fs.media.context.TransContext;

/**
 * 进度处理器。
 */
public class ProgressHandler {

	/**
	 * 默认间隔时间
	 */
	private static final int DEFAULT_INTERVAL = 10000;

	/**
	 * 上次时间
	 */
	private long lasttime;
	/**
	 * 发送进度消息的时间间隔
	 */
	private long interval = DEFAULT_INTERVAL;
	/**
	 * 百分比
	 */
	private int percent = 0;
	/**
	 * 转换任务上下文
	 */
	private TransContext context;
	/**
	 * 转换服务
	 */
	private TransformService service;

	public ProgressHandler(TransContext context, TransformService service) {
		this.context = context;
		this.service = service;
	}

	public int getPercent() {
		return this.percent;
	}

	/**
	 * 接收时，设置进度为1，同时发送进度消息
	 */
	public void onReceived() {
		this.changePercent(1, true);
	}


	public void onDownloadProgress(long current, long total) {
		this.guessPercent(current, total, 1, 10);
	}

	public void onBeforeTranscode() {
		this.changePercent(10, true);
	}

	public void onTranscodeProgress(long current, long total) {
		this.guessPercent(current, total, 10, 90);
	}

	public void onAfterTranscode() {
		this.changePercent(90, true);
	}

	public void onUploadProgress(long current, long total) {
		this.guessPercent(current, total, 90, 99);
	}

	public void onRespond() {
		this.changePercent(99, true);
	}

	/**
	 * 估算并修改进度百分比
	 *
	 * @param current
	 * @param total
	 * @param base
	 * @param peak
	 */
	private void guessPercent(long current, long total, int base, int peak) {
		int percent = (int) (base + current * (peak - base) / total);
		this.changePercent(percent, false);
	}

	/**
	 * 修改进度百分比。
	 *
	 * @param percent 百分比
	 * @param force   是否强制修改
	 */
	private void changePercent(int percent, boolean force) {
		this.percent = percent;
		long thistime = System.currentTimeMillis();
		//上次发送时间为0，或者上次发送距离现在超过时间间隔，则发送进度消息
		if (force || this.lasttime == 0 || lasttime + interval <= thistime) {
			lasttime = thistime;
			//发送进度消息
			this.service.sendProgressMsg(context, percent);
		}
	}
}

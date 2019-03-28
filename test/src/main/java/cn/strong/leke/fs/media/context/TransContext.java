package cn.strong.leke.fs.media.context;

import java.io.File;
import java.util.Date;

/**
 * 转换任务上下文，包含任务所有信息。
 */
public class TransContext {

	// 工作线程信息
	/**
	 * 工作线程名
	 */
	private String workerId;
	/**
	 * 设置工作目录
	 */
	private File workDirectory;
	/**
	 * 接收时间
	 */
	private Date acceptOn;
	/**
	 * 转换结束，响应时间
	 */
	private Date replyOn;

	// 指令信息
	private boolean ignoreVcodec;
	private boolean ignoreAcodec;

	/**
	 * 文件信息
	 */
	private FileInfo fileInfo;
	/**
	 * 媒体信息
	 */
	private MediaInfo mediaInfo;

	public String getWorkerId() {
		return workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	public File getWorkDirectory() {
		return workDirectory;
	}

	public void setWorkDirectory(File workDirectory) {
		this.workDirectory = workDirectory;
	}

	public Date getAcceptOn() {
		return acceptOn;
	}

	public void setAcceptOn(Date acceptOn) {
		this.acceptOn = acceptOn;
	}

	public Date getReplyOn() {
		return replyOn;
	}

	public void setReplyOn(Date replyOn) {
		this.replyOn = replyOn;
	}

	public boolean isIgnoreVcodec() {
		return ignoreVcodec;
	}

	public void setIgnoreVcodec(boolean ignoreVcodec) {
		this.ignoreVcodec = ignoreVcodec;
	}

	public boolean isIgnoreAcodec() {
		return ignoreAcodec;
	}

	public void setIgnoreAcodec(boolean ignoreAcodec) {
		this.ignoreAcodec = ignoreAcodec;
	}

	public FileInfo getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}

	public MediaInfo getMediaInfo() {
		return mediaInfo;
	}

	public void setMediaInfo(MediaInfo mediaInfo) {
		this.mediaInfo = mediaInfo;
	}

	public File createFile(String fileName) {
		return new File(this.workDirectory, fileName);
	}

	public String createPath(String fileName) {
		return this.workDirectory.getAbsolutePath() + File.separator + fileName;
	}
}

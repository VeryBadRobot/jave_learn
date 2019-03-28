package cn.strong.leke.fs.media.model;

import java.util.Date;

/**
 * 媒体转换结果消息。
 */
public class TransRespMsg {

	/**
	 * 文件ID
	 */
	private String fileId;
	/**
	 * 是否转换成功
	 */
	private Boolean success;
	/**
	 * 错误码
	 */
	private Integer errCode;
	/**
	 * 错误信息
	 */
	private String errInfo;
	/**
	 * 错误日志
	 */
	private String errLog;
	/**
	 * 转换后的m3u8路径
	 */
	private String m3u8Path;
	/**
	 * 文件质量
	 */
	private Integer quality;
	/**
	 * 音频时长
	 */
	private Integer duration;
	/**
	 * 节点ID
	 */
	private String nodeId;
	/**
	 * 线程名称
	 */
	private String workerId;
	/**
	 * 接受时间
	 */
	private Date acceptOn;
	/**
	 * 响应时间
	 */
	private Date replyOn;


	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Integer getErrCode() {
		return errCode;
	}

	public void setErrCode(Integer errCode) {
		this.errCode = errCode;
	}

	public String getErrInfo() {
		return errInfo;
	}

	public void setErrInfo(String errInfo) {
		this.errInfo = errInfo;
	}

	public String getErrLog() {
		return errLog;
	}

	public void setErrLog(String errLog) {
		this.errLog = errLog;
	}

	public String getM3u8Path() {
		return m3u8Path;
	}

	public void setM3u8Path(String m3u8Path) {
		this.m3u8Path = m3u8Path;
	}

	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getWorkerId() {
		return workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
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
}

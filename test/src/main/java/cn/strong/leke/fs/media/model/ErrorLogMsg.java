package cn.strong.leke.fs.media.model;

import java.util.Date;

/**
 * 错误日志消息。
 */
public class ErrorLogMsg {

	/**
	 * 文件ID
	 */
	private String fileId;
	/**
	 * 错误页码
	 */
	private Integer pageNo;
	/**
	 * 出错节点
	 */
	private String nodeId;
	/**
	 * 节点类型
	 */
	private Integer nodeType;
	/**
	 * 线程名
	 */
	private String workerId;
	/**
	 * 错误时间
	 */
	private Date errTime;
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

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public Integer getNodeType() {
		return nodeType;
	}

	public void setNodeType(Integer nodeType) {
		this.nodeType = nodeType;
	}

	public String getWorkerId() {
		return workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	public Date getErrTime() {
		return errTime;
	}

	public void setErrTime(Date errTime) {
		this.errTime = errTime;
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
}

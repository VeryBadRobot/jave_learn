package cn.strong.leke.fs.media.model;

/**
 * 转换进度消息对象。
 */
public class ProgressMsg {

	/**
	 * 文件ID
	 */
	private String fileId;
	/**
	 * 节点地址
	 */
	private String nodeId;
	/**
	 * 线程名
	 */
	private String workerId;
	/**
	 * 进度，百分比
	 */
	private Integer progress;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
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

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}
}

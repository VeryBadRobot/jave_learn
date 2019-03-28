package cn.strong.leke.fs.media.model;

import cn.strong.leke.fs.media.context.FileInfo;

/**
 * 媒体转换请求消息。
 */
public class TransReqMsg {

	/**
	 * 文件ID
	 */
	private String fileId;
	/**
	 * 文件路径
	 */
	private String path;
	/**
	 * 文件扩展名
	 */
	private String ext;
	/**
	 * 文件尺寸
	 */
	private Long size;
	/**
	 * 文件类型
	 */
	private String type;
	/**
	 * 尝试次数
	 */
	private Integer retryCount;
	/**
	 * 上一次处理的节点Id
	 */
	private String nodeId;
	/**
	 * 最大尝试次数
	 */
	private Integer maxRetryCount;
	/**
	 * 消息ID
	 */
	private String messageId;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public Integer getMaxRetryCount() {
		return maxRetryCount;
	}

	public void setMaxRetryCount(Integer maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * 设置文件信息,ID,path,Size,Ext,Type
	 *
	 * @return
	 */
	public FileInfo toFileInfo() {
		FileInfo fileInfo = new FileInfo();
		fileInfo.setFileId(this.getFileId());
		fileInfo.setPath(this.getPath());
		fileInfo.setSize(this.getSize());
		fileInfo.setExt(this.getExt());
		fileInfo.setType(this.getType());
		return fileInfo;
	}
}

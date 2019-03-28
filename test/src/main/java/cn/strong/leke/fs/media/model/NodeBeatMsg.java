package cn.strong.leke.fs.media.model;

import java.util.Date;

/**
 * 节点心跳消息。
 */
public class NodeBeatMsg {

	/**
	 * 节点地址
	 */
	private String nodeId;
	/**
	 * 节点类型
	 */
	private Integer nodeType;
	/**
	 * 工作线程数
	 */
	private Integer workNum;
	/**
	 * 健康程度
	 */
	private Boolean isHealth;
	/**
	 * IP地址与nodeID一致
	 */
	private String address;
	/**
	 * 心跳次数
	 */
	private Integer beatNo;
	/**
	 * 心跳时间
	 */
	private Date beatOn;

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

	public Integer getWorkNum() {
		return workNum;
	}

	public void setWorkNum(Integer workNum) {
		this.workNum = workNum;
	}

	public Boolean getIsHealth() {
		return isHealth;
	}

	public void setIsHealth(Boolean isHealth) {
		this.isHealth = isHealth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getBeatNo() {
		return beatNo;
	}

	public void setBeatNo(Integer beatNo) {
		this.beatNo = beatNo;
	}

	public Date getBeatOn() {
		return beatOn;
	}

	public void setBeatOn(Date beatOn) {
		this.beatOn = beatOn;
	}
}

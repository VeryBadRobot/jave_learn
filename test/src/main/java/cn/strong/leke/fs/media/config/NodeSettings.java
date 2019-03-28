package cn.strong.leke.fs.media.config;

import java.io.File;

/**
 * 节点设置
 */
public class NodeSettings {
	/**
	 * 节点ID
	 */
	private String nodeId;
	/**
	 * 节点IP
	 */
	private String address;
	/**
	 * 转换目录
	 */
	private File swapDir;

	/**
	 * 核心数
	 */
	private Integer cores;
	/**
	 * 线程数
	 */
	private Integer works;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public File getSwapDir() {
		return swapDir;
	}

	public void setSwapDir(File swapDir) {
		this.swapDir = swapDir;
	}

	public Integer getCores() {
		return cores;
	}

	public void setCores(Integer cores) {
		this.cores = cores;
	}

	public Integer getWorks() {
		return works;
	}

	public void setWorks(Integer works) {
		this.works = works;
	}
}

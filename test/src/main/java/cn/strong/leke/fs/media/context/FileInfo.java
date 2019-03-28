package cn.strong.leke.fs.media.context;

import java.io.File;

/**
 * 转换文件信息。
 */
public class FileInfo {

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
	 * 文件大小
	 */
	private Long size;
	/**
	 * 文件类型
	 */
	private String type;
	/**
	 * 实际文件
	 */
	private File file;

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

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String buildFileName(String name) {
		return name + "." + this.ext;
	}
}

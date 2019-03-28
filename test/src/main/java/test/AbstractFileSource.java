package test;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 抽象文件描述，处理文件名称和后辍。
 */
public abstract class AbstractFileSource implements FileSource {
	/**
	 * 文件名称
	 */
	private String name;
	/**
	 * 扩展名
	 */
	private String ext;

	public AbstractFileSource(String fullName) {
		this.name = FilenameUtils.getBaseName(fullName);
		this.ext = StringUtils.lowerCase(FilenameUtils.getExtension(fullName));
	}

	public AbstractFileSource(String name, String ext) {
		this.name = name;
		this.ext = ext;
	}

	public String getName() {
		return name;
	}

	public String getExt() {
		return ext;
	}
}

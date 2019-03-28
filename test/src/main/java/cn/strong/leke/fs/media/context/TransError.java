package cn.strong.leke.fs.media.context;

import cn.strong.leke.fs.media.service.TransformException;

/**
 * 转换异常定义。
 */
public enum TransError {
	/**
	 * 50000,未知错误
	 */
	UNKNOWN("50000", "未知错误"),
	/**
	 * 50001,文件下载错误
	 */
	DOWNLOAD("50001", "文件下载错误"),
	/**
	 * 50002，文件转码错误
	 */
	TRANSCODE("50002", "文件转码错误"),
	/**
	 * 50003，文件上传错误
	 */
	UPLOAD("50003", "文件上传异常"),
	/**
	 * 50101，文件内容非法
	 */
	FILE_INVALID("50101", "文件内容非法");

	/**
	 * 错误码
	 */
	public final String code;
	/**
	 * 信息
	 */
	public final String info;

	private TransError(String code, String info) {
		this.code = code;
		this.info = info;
	}

	public TransformException newTransformException() {
		return new TransformException(this.code);
	}

	public TransformException newTransformException(Throwable t) {
		return new TransformException(this.code, t);
	}

	public static String getInfo(String code) {
		TransError error = null;
		for (TransError v : values()) {
			if (v.code.equals(code)) {
				error = v;
				break;
			}
		}
		return error != null ? error.info : UNKNOWN.info;
	}
}

package cn.strong.leke.fs.media.service;

/**
 * 转码异常，封装异常日志。
 */
public class CommandException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CommandException(String message) {
		super(message);
	}

	public CommandException(String message, Throwable cause) {
		super(message);
	}
}

package cn.strong.leke.fs.media.service;

/**
 * 转换异常。
 * 该异常发生后，放弃当前转换任务的处理，但任务不会返回队列。
 */
public class TransformException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public TransformException() {
		super();
	}

	public TransformException(String message) {
		super(message);
	}

	public TransformException(Throwable cause) {
		super(cause);
	}

	public TransformException(String message, Throwable cause) {
		super(message, cause);
	}
}

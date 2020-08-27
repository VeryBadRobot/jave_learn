package processbuilder;


import com.sun.javafx.collections.MappingChange;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 既然有Process类，那为什么还要发明个ProcessBuilder类呢？ProcessBuilder和Process两个类有什么区别呢？
 * 原来，ProcessBuilder为进程提供了更多的控制，例如，可以设置当前工作目录，还可以改变环境参数。而Process的功能相对来说简单的多。
 * ProcessBuilder是一个final类，有两个带参数的构造方法，你可以通过构造方法来直接创建ProcessBuilder的对象。
 * 而Process是一个抽象类，一般都通过Runtime.exec()和ProcessBuilder.start()来间接创建其实例。
 * <p>
 * 注意：
 * 修改进程构建器的属性将影响后续由该对象的 start() 方法启动的进程，但从不会影响以前启动的进程或 Java 自身的进程。
 * ProcessBuilder类不是同步的。如果多个线程同时访问一个 ProcessBuilder，而其中至少一个线程从结构上修改了其中一个属性，它必须保持外部同步。
 */
public class ProcessbuilderExample {
	public static final Logger logger = Logger.getLogger("test");

	public static void main(String[] args) {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder("notepad.exe", "testfile");
			Map<String, String> env = processBuilder.environment();
			System.out.println(env);
			processBuilder.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int doWaitFor(Process process) {
		int exitValue = -1; //returned to caller when p is finished
		InputStream error = process.getErrorStream();
		InputStream is = process.getInputStream();
		byte[] b = new byte[1024];
		int readbytes = -1;
		try {
			while ((readbytes = error.read(b)) != -1) {
				logger.info("标准错误输出信息： " + new String(b, 0, readbytes));
			}

			while ((readbytes = is.read(b)) != -1) {
				logger.info("标准输出信息：" + new String(b, 0, readbytes));
			}
		} catch (IOException e) {
			logger.info("等待进程结束出现错误！");
			e.printStackTrace();
		} finally {
			try {
				error.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return exitValue;
	}


}

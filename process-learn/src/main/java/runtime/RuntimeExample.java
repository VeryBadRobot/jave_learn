package runtime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * 进程的标准流有三个，输入流，输出流，错误流。
 * 推荐使用ProcessBuilder，使用起来更方便。
 * <!---->1、 <!---->在一个外部进程执行完之前你不能得到他的退出状态
 * <p>
 * <!---->2、 <!---->在你的外部程序开始执行的时候你必须马上控制输入、输出、出错这些流。
 * <p>
 * <!---->3、 <!---->你必须用Runtime.exec()去执行程序
 * <p>
 * <!---->4、 <!---->你不能象命令行一样使用Runtime.exec()。
 */
public class RuntimeExample {

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println(System.getProperty("os.name"));
	}


	/**
	 * 运行指定位置的应用程序
	 *
	 * @throws IOException
	 */
	public static void runExe() throws IOException {
		Runtime runtime = Runtime.getRuntime();
		String exePath = "D:\\Program Files (x86)\\XMind\\XMind.exe";
		runtime.exec(exePath);
	}

	/**
	 * 执行bat文件通过字符流获取CMD输出
	 */
	public static void getCMDOutput() {
		Process process;
		String cmd = ".\\process-learn\\target\\classes\\data\\ipinfo.bat";

		try {
			//执行命令
			process = Runtime.getRuntime().exec(cmd);
			//取得命令结果的输出流
			InputStream fis = process.getInputStream();
			//用一个读输出流类去读
			//CMD默认编码为GBK，否则会乱码
			InputStreamReader inputStreamReader = new InputStreamReader(fis, "GBK");
			//用缓冲器读行
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line = null;
			//直到读完为止
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}

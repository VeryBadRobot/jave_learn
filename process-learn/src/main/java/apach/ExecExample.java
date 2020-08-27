package apach;

import org.apache.commons.exec.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Java管理进程，API级别是使用：Runtime.getRuntime().exec(“shell”);这个方法。
 * Java在执行命令时输出到某个Buffer里，这个Buffer是有容量限制的，如果满了一直没读取，就会一直等待，造成进程锁死的现象。
 * 使用Apache Commons Exec，应该可以避免很多类似的坑。
 * 它提供一些常用的方法用来执行外部进程，另外，它提供了监视狗Watchdog来设监视进程的执行超时，同时也还实现了同步和异步功能，
 * Apache Commons Exec涉及到多线程，比如新启动一个进程，Java中需要再开三个线程来处理进程的三个数据流，分别是标准输入，标准输出和错误输出。
 * 基本用法
 * 就三步：
 * 1）创建命令行：CommandLine
 * 2）创建执行器：DefaultExecutor
 * 3）用执行器执行命令：executor.execute(cmdLine);
 * <p>
 * String cmdStr = "ping www.baidu.com -t";
 * final CommandLine cmdLine = CommandLine.parse(cmdStr);
 * DefaultExecutor executor = new DefaultExecutor();
 * int exitValue = executor.execute(cmdLine);
 */
public class ExecExample {

	public static void main(String[] args) throws IOException, InterruptedException {
		Map map = new HashMap();
		map.put("file", new File("invoice.pdf"));
		CommandLine cmdLine = new CommandLine("ArcroRd32.exe");
		cmdLine.addArgument("/p");
		cmdLine.addArgument("/h");
		cmdLine.addArgument("${file}");
		cmdLine.setSubstitutionMap(map);

		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();


		//执行器
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

		PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream,errorStream);

		executor.setStreamHandler(pumpStreamHandler);

		//监视器
		ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
		executor.setWatchdog(watchdog);

		executor.execute(cmdLine, resultHandler);

		int exitValue = resultHandler.getExitValue();
	}
}
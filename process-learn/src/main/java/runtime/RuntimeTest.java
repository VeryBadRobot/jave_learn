package runtime;

import java.io.*;
import java.util.concurrent.CountDownLatch;


public class RuntimeTest {

	public static void main(String[] args) throws IOException, InterruptedException {
//		long start = System.currentTimeMillis();
//		final String[] path = new String[]{
//				"F:\\test\\（精校版）2019年全国卷Ⅰ理数高考试题文档版（含答案）.docx",
//				"F:\\test\\（精校版）2019年全国卷Ⅰ文数高考试题文档版（含答案）.docx",
//				"F:\\test\\（精校版）2019年全国卷Ⅱ理数高考试题文档版（含答案）.docx",
//				"F:\\test\\（精校版）2019年全国卷Ⅱ文数高考试题文档版（含答案）.docx",
//				"F:\\test\\（精校版）2019年全国卷Ⅲ理数高考试题文档版（含答案）.docx",
//				"F:\\test\\（精校版）2019年天津卷理数高考试题文档版（含答案）.docx",
//				"F:\\test\\（精校版）2019年天津卷文数高考试题文档版（含答案）.docx",
//				"F:\\test\\（精校版）2019年新课标Ⅲ文数高考试题文档版（含答案）.doc",
//				"F:\\test\\（精校版）2019年浙江卷数学高考试题文档版（含答案）.doc",
//		};
//		final CountDownLatch countDownLatch = new CountDownLatch(9);
//		Thread[] threads = new Thread[9];
//		for (int i = 0; i < 9; i++) {
//			final String p = path[i];
//			threads[i] = new Thread() {
//				@Override
//				public void run() {
//					try {
//						convert(p);
//						countDownLatch.countDown();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			};
//
//		}
//
//		for (Thread thread : threads) {
//			thread.start();
//		}
//		countDownLatch.await();
//		System.out.println("t-------------- : " + (System.currentTimeMillis() - start));


		convert("F:\\test\\（精校版）2019年全国卷Ⅰ理数高考试题文档版（含答案）.docx");
		convert("F:\\test\\（精校版）2019年全国卷Ⅰ理数高考试题文档版（含答案）.doc");
	}


	public static void convert(final String path) throws IOException {
		Runtime runtime = Runtime.getRuntime();
		String[] cmdArray = new String[]{"cmd ", "run ", "D:\\vsProject\\ConsoleApp1\\bin\\Debug\\ConsoleApp1.exe"};
		Process p = runtime.exec("D:\\vsProject\\ConsoleApp1\\bin\\Debug\\ConsoleApp1.exe");// 启动另一个进程来执行命令
		long start = System.currentTimeMillis();
		try {
			final OutputStreamWriter bufferedWriter = new OutputStreamWriter(p.getOutputStream(), "gbk");
			Thread thread = new Thread() {
				@Override
				public void run() {
					try {
						bufferedWriter.write(path);

						bufferedWriter.write("\r\n");
						bufferedWriter.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			thread.start();
			BufferedInputStream in = new BufferedInputStream(p.getInputStream());
			BufferedReader inBr = new BufferedReader(new InputStreamReader(in, "GBK"));
			String lineStr;
			while ((lineStr = inBr.readLine()) != null)
				//获得命令执行后在控制台的输出信息
				System.out.println(lineStr);// 打印输出信息

			//检查命令是否执行失败。
			if (p.waitFor() != 0) {
				if (p.exitValue() == 1)//p.exitValue()==0表示正常结束，1：非正常结束
					System.err.println("命令执行失败!");
			}

			inBr.close();
			in.close();
			p.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("total : " + (System.currentTimeMillis() - start));
	}


}

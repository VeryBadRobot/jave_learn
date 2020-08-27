package runtime;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Converter {

	public static void main(String[] args) throws IOException, InterruptedException {
//		System.out.println(Runtime.getRuntime().availableProcessors() * 2);
////		long start = System.currentTimeMillis();
////		final String[] path = new String[]{
////				"F:\\test\\（精校版）2019年全国卷Ⅰ理数高考试题文档版（含答案）.docx",
////				"F:\\test\\（精校版）2019年全国卷Ⅰ文数高考试题文档版（含答案）.docx",
////				"F:\\test\\（精校版）2019年全国卷Ⅱ理数高考试题文档版（含答案）.docx",
////				"F:\\test\\（精校版）2019年全国卷Ⅱ文数高考试题文档版（含答案）.docx",
////				"F:\\test\\（精校版）2019年全国卷Ⅲ理数高考试题文档版（含答案）.docx",
////				"F:\\test\\（精校版）2019年天津卷理数高考试题文档版（含答案）.docx",
////				"F:\\test\\（精校版）2019年天津卷文数高考试题文档版（含答案）.docx",
////				"F:\\test\\（精校版）2019年新课标Ⅲ文数高考试题文档版（含答案）.doc",
////				"F:\\test\\（精校版）2019年浙江卷数学高考试题文档版（含答案）.doc",
////		};
////		final CountDownLatch countDownLatch = new CountDownLatch(9);
////		Thread[] threads = new Thread[9];
////		for (int i = 0; i < 9; i++) {
////			final String p = path[i];
////			threads[i] = new Thread() {
////				@Override
////				public void run() {
////					try {
////						convert(p);
////						countDownLatch.countDown();
////					} catch (IOException e) {
////						e.printStackTrace();
////					} catch (InterruptedException e) {
////						e.printStackTrace();
////					}
////				}
////			};
////
////		}
////
////		for (Thread thread : threads) {
////			thread.start();
////		}
////		countDownLatch.await();
////		System.out.println("t-------------- : " + (System.currentTimeMillis() - start));
		String path = "F:\\test\\（精校版）2019年全国卷Ⅰ理数高考试题文档版（含答案）.docx";
		String putPath1 = "F:\\test\\1.doc";
		String outPath2 = "F:\\test\\2.doc";
		originalNio(path, putPath1);
		whileNio(path, outPath2);
	}

	public static void charSet(String path) throws UnsupportedEncodingException {
		String t = path;

		String utf8 = new String(path.getBytes("GBK"));

		System.out.println(utf8);


	}


	public static void convert(String path) throws IOException, InterruptedException {

		charSet(path);
		if (true)
			return;

		long start = System.currentTimeMillis();
		List<String> list = new ArrayList<>();

		list.add("D:\\vsProject\\ConsoleApp1\\bin\\Debug\\ConsoleApp1.exe");
		list.add(path);
		ProcessBuilder ps = new ProcessBuilder(list);
		Process process = ps.start();
		process.waitFor();

		BufferedInputStream in = new BufferedInputStream(process.getInputStream());
		BufferedReader inBr = new BufferedReader(new InputStreamReader(in, "GBK"));
		String lineStr;
		while ((lineStr = inBr.readLine()) != null)
			//获得命令执行后在控制台的输出信息
			System.out.println(lineStr);// 打印输出信息

		//检查命令是否执行失败。
		if (process.waitFor() != 0) {
			if (process.exitValue() == 1)//p.exitValue()==0表示正常结束，1：非正常结束
				System.err.println("命令执行失败!");
		}

		inBr.close();
		in.close();
		process.destroy();

		System.out.println("total : " + (System.currentTimeMillis() - start));
	}

	public static void originalNio(String path, String outPath) throws IOException {

		long beginTime = System.currentTimeMillis();

		FileChannel fc = new FileInputStream(new File(path)).getChannel();

		FileChannel fco = new FileOutputStream(new File(outPath)).getChannel();

		fco.transferFrom(fc, 0, fc.size());

		fc.close();
		fco.close();
		long endTime = System.currentTimeMillis();


		System.out.println("采用NIO FileChannel 自带方法  读取，耗时："
				+ (endTime - beginTime));
	}

	public static void whileNio(String path, String outPath) throws IOException {
		long beginTime = System.currentTimeMillis();

		FileChannel fc = new FileInputStream(new File(path)).getChannel();

		FileChannel fco = new FileOutputStream(new File(outPath))
				.getChannel();

		ByteBuffer buf = ByteBuffer.allocate(1024);

		while (fc.read(buf) != -1) {
			buf.flip();
			fco.write(buf);
			buf.clear();
		}

		fc.close();
		fco.close();

		long endTime = System.currentTimeMillis();

		System.out.println("采用NIO FileChannel 循环 读取，耗时："
				+ (endTime - beginTime));
	}


}

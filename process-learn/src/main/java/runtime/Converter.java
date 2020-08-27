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
////				"F:\\test\\����У�棩2019��ȫ����������߿������ĵ��棨���𰸣�.docx",
////				"F:\\test\\����У�棩2019��ȫ����������߿������ĵ��棨���𰸣�.docx",
////				"F:\\test\\����У�棩2019��ȫ����������߿������ĵ��棨���𰸣�.docx",
////				"F:\\test\\����У�棩2019��ȫ����������߿������ĵ��棨���𰸣�.docx",
////				"F:\\test\\����У�棩2019��ȫ����������߿������ĵ��棨���𰸣�.docx",
////				"F:\\test\\����У�棩2019�����������߿������ĵ��棨���𰸣�.docx",
////				"F:\\test\\����У�棩2019�����������߿������ĵ��棨���𰸣�.docx",
////				"F:\\test\\����У�棩2019���¿α�������߿������ĵ��棨���𰸣�.doc",
////				"F:\\test\\����У�棩2019���㽭����ѧ�߿������ĵ��棨���𰸣�.doc",
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
		String path = "F:\\test\\����У�棩2019��ȫ����������߿������ĵ��棨���𰸣�.docx";
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
			//�������ִ�к��ڿ���̨�������Ϣ
			System.out.println(lineStr);// ��ӡ�����Ϣ

		//��������Ƿ�ִ��ʧ�ܡ�
		if (process.waitFor() != 0) {
			if (process.exitValue() == 1)//p.exitValue()==0��ʾ����������1������������
				System.err.println("����ִ��ʧ��!");
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


		System.out.println("����NIO FileChannel �Դ�����  ��ȡ����ʱ��"
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

		System.out.println("����NIO FileChannel ѭ�� ��ȡ����ʱ��"
				+ (endTime - beginTime));
	}


}

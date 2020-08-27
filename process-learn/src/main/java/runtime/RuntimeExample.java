package runtime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * ���̵ı�׼���������������������������������
 * �Ƽ�ʹ��ProcessBuilder��ʹ�����������㡣
 * <!---->1�� <!---->��һ���ⲿ����ִ����֮ǰ�㲻�ܵõ������˳�״̬
 * <p>
 * <!---->2�� <!---->������ⲿ����ʼִ�е�ʱ����������Ͽ������롢�����������Щ����
 * <p>
 * <!---->3�� <!---->�������Runtime.exec()ȥִ�г���
 * <p>
 * <!---->4�� <!---->�㲻����������һ��ʹ��Runtime.exec()��
 */
public class RuntimeExample {

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println(System.getProperty("os.name"));
	}


	/**
	 * ����ָ��λ�õ�Ӧ�ó���
	 *
	 * @throws IOException
	 */
	public static void runExe() throws IOException {
		Runtime runtime = Runtime.getRuntime();
		String exePath = "D:\\Program Files (x86)\\XMind\\XMind.exe";
		runtime.exec(exePath);
	}

	/**
	 * ִ��bat�ļ�ͨ���ַ�����ȡCMD���
	 */
	public static void getCMDOutput() {
		Process process;
		String cmd = ".\\process-learn\\target\\classes\\data\\ipinfo.bat";

		try {
			//ִ������
			process = Runtime.getRuntime().exec(cmd);
			//ȡ���������������
			InputStream fis = process.getInputStream();
			//��һ�����������ȥ��
			//CMDĬ�ϱ���ΪGBK�����������
			InputStreamReader inputStreamReader = new InputStreamReader(fis, "GBK");
			//�û���������
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line = null;
			//ֱ������Ϊֹ
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}

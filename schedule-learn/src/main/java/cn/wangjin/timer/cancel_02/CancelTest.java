package cn.wangjin.timer.cancel_02;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;

public class CancelTest {

	public static void main(String[] args) throws InterruptedException {
		//1.创建一个Timer实例
		Timer timer = new Timer();
		//2.创建二个TimerTask的实例
		MyTimerTask_02 myTimerTask = new MyTimerTask_02("NO.1");
		MyTimerTask_02 myTimerTask_02 = new MyTimerTask_02("NO.2");


		/**
		 * 获取当前时间，并设置成3秒之后的时间。
		 * 如当前时间为：  2018-11-10 23:59:57
		 * 则设置后的时间为：2018-11-11 00:00:00
		 */
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//输出当前时间
		System.out.println(sf.format(calendar.getTime()));


		/**
		 * 等待delay毫秒后执行一次task，之后每隔period毫秒执行一次task。
		 * 如当前时间为2018-11-11 00:00:00,
		 * 则在2018-11-11 00:00:01执行一次task。之后每隔2秒执行一次task。
		 *
		 */
		timer.schedule(myTimerTask, 3000L, 2000L);
		timer.schedule(myTimerTask_02, 2000L, 1000L);

		//主线程沉睡5秒
		Thread.sleep(5000L);

		/**
		 * 取消所有当前未执行的task任务，已经执行和正在执行的task不受影响。可以多次调用，但是只有第一次有效。
		 * Timer的cancel是取消timer上的所有task。
		 * TimerTask的cancel是取消一个task。
		 */
		timer.cancel();
	}
}

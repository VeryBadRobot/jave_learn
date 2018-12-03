package wangjin.timer.cancel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;

public class MyTimer {


	public static void main(String[] args) {
		//1.创建一个Timer实例
		Timer timer = new Timer();
		//2.创建一个TimerTask的实例
		MyTimerTask myTimerTask = new MyTimerTask("NO.1");
		//3.通过Timer定时定频率调用MyTimerTask的业务逻辑



		/**
		 * 获取当前时间，并设置成3秒之后的时间。
		 * 如当前时间为：  2018-11-10 23:59:57
		 * 则设置后的时间为：2018-11-11 00:00:00
		 */
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//输出当前时间
		System.out.println(sf.format(calendar.getTime()));



		//-------------------schedule的用法4. schedule(TimerTask task, long delay, long period)----------------------------//
		/**
		 * 等待delay毫秒后执行一次task，之后每隔period毫秒执行一次task。
		 * 如当前时间为2018-11-11 00:00:00,
		 * 则在2018-11-11 00:00:01执行一次task。之后每隔2秒执行一次task。
		 *
		 */
		myTimerTask.setName("schedule-4");
		timer.schedule(myTimerTask, 1000L, 2000L);

	}
}

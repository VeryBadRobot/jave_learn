package wangjin.timer;

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
		//即第一次执行是在当前时间的2秒之后，然后每隔1秒执行一次
		//timer.schedule(myTimerTask, 2000L, 1000L);


		/**
		 * 获取当前时间，并设置成3秒之后的时间。
		 * 如当前时间为：  2018-11-10 23:59:57
		 * 则设置后的时间为：2018-11-11 00:00:00
		 */
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//输出当前时间
		System.out.println(sf.format(calendar.getTime()));

		//当前时间加3秒
		calendar.add(Calendar.SECOND, 3);

		//-------------------schedule的用法1. schedule(TimerTask task, Date time)----------------------------//
		/**
		 * 在时间等于或者超过time的时候执行，且仅执行一次
		 * 如在2018-11-11 00:00:00执行一次任务，打印任务名和当前时间。
		 */
//		myTimerTask.setName("schedule-1");
//		timer.schedule(myTimerTask, calendar.getTime());

		//-------------------schedule的用法2. schedule(TimerTask task, Date firstTime, long period)----------------------------//
		/**
		 * 在时间等于或者超过firstTime的时候首次执行task，之后每隔period毫秒秒执行一次task。
		 * 如在2018-11-11 00:00:00执行一次任务，打印任务名和当前时间。之后每隔2秒执行一次task。
		 */

//		myTimerTask.setName("schedule-2");
//		timer.schedule(myTimerTask, calendar.getTime(), 2000L);

		//-------------------schedule的用法3. schedule(TimerTask task, long delay)----------------------------//
		/**
		 * 等待delay毫秒后执行一次task。
		 * 如当前时间为2018-11-11 00:00:00,
		 * 则在2018-11-11 00:00:01执行一次task。
		 *
		 */
//		myTimerTask.setName("schedule-3");
//		timer.schedule(myTimerTask, 1000L);


		//-------------------schedule的用法4. schedule(TimerTask task, long delay, long period)----------------------------//
		/**
		 * 等待delay毫秒后执行一次task，之后每隔period毫秒执行一次task。
		 * 如当前时间为2018-11-11 00:00:00,
		 * 则在2018-11-11 00:00:01执行一次task。之后每隔2秒执行一次task。
		 *
		 */
		myTimerTask.setName("schedule-4");
		timer.schedule(myTimerTask, 1000L, 2000L);

		//-------------------scheduleAtFixRate的用法1. scheduleAtFixedRate(TimerTask task, Date firstTime,long period)----------------------------//
		/**
		 * 在时间等于或者超过firstTime的时候首次执行task，之后每隔period毫秒秒执行一次task。
		 * 如在2018-11-11 00:00:00执行一次任务，打印任务名和当前时间。之后每隔2秒执行一次task。
		 */
		myTimerTask.setName("scheduleAtFixRate-1");
		timer.scheduleAtFixedRate(myTimerTask, calendar.getTime(), 2000L);

		//-------------------scheduleAtFixRate的用法2. scheduleAtFixedRate(TimerTask task, long delay, long period)----------------------------//
		/**
		 * 等待delay毫秒后执行一次task，之后每隔period毫秒执行一次task。
		 * 如当前时间为2018-11-11 00:00:00,
		 * 则在2018-11-11 00:00:01执行一次task。之后每隔2秒执行一次task。
		 *
		 */
		myTimerTask.setName("scheduleAtFixRate-2");
		timer.schedule(myTimerTask, 1000L, 2000L);
	}
}

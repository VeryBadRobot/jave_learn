package cn.wangjin.timer.cancel_01;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

public class MyTimerTask_01 extends TimerTask {

	/**
	 * 任务名
	 */
	private String name;
	/**
	 * 实例执行次数
	 */
	private Integer count = 0;

	public MyTimerTask_01(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		if (count < 3) {
			//以yyyy-MM-dd HH:mm:ss格式打印当前的时间 (月份与分钟都是m开头，为了区分月份用M,分钟用m)
			//2018-11-11 00:00:00
			//获取日历对象
			Calendar calendar = Calendar.getInstance();
			//日期格式化对象
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//输出当前时间
			System.out.println("current time is : " + sf.format(calendar.getTime()));
			//输出当前任务名
			System.out.println("current task name is : " + name);
			count++;
		} else {
			//取消当前task
			cancel();
			System.out.println("Task Cancel！");
		}


	}
}

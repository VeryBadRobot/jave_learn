package test;

import cn.strong.leke.common.serialize.support.json.JsonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataTest {
	static int limit = 5;


	static int init = 5;
	static int add = 10;
	static int sub = 5;
	static int current = 64;

	static double max = 120;
	static double min = 20;


	public static void main(String[] args) throws FileNotFoundException {
		test();
	}


	public static void test() throws FileNotFoundException {
		InputStream inputStream = new FileInputStream(new File("D:\\Leke\\leke-datagov\\target\\test-classes\\office.json"));
		DataPoints dataPoints = JsonUtils.fromJSON(inputStream, DataPoints.class);
		for (DataPoint d:dataPoints.getDatapoints())
		{
			d.setCurrent(current);
		}
		print(dataPoints);
		System.out.println("--------------------------------------------------------------");
		adjust(dataPoints);
	}


	public static void adjust(DataPoints data) {
		List<Double> total = new ArrayList<>(data.getDatapoints().size());

		DataPoints points = new DataPoints();
		points.setMetric_name(data.getMetric_name());
		List<DataPoint> dataPoints = new ArrayList<>(data.getDatapoints().size());
		points.setDatapoints(dataPoints);

		for (DataPoint d : data.getDatapoints()) {
			DataPoint p = new DataPoint();
			p.setTimestamp(d.getTimestamp());
			p.setUnit(d.getUnit());
			double m = d.getAverage() * current / 100;
			double use = (m * 100)/ init;
			p.setAverage(use);
			p.setCurrent(init);
			if (use > max) {
				init += add;
			} else if (use < min) {
				if (init > limit) {
					init -= sub;
				}
			}
			dataPoints.add(p);
		}

		print(points);

	}


	public static void print(DataPoints dataPoints) {
		System.out.println(dataPoints.getMetric_name());
		for (DataPoint d : dataPoints.getDatapoints()) {
			Date date = new Date(d.getTimestamp());
			System.out.println(date.toString() + " - " + d.getAverage() + d.getUnit()  + " - " + d.getCurrent());
		}
	}


}

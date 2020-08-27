package test;

import java.util.List;

public class DataPoints {

	private List<DataPoint> datapoints;
	private String metric_name;


	public List<DataPoint> getDatapoints() {
		return datapoints;
	}

	public void setDatapoints(List<DataPoint> datapoints) {
		this.datapoints = datapoints;
	}

	public String getMetric_name() {
		return metric_name;
	}

	public void setMetric_name(String metric_name) {
		this.metric_name = metric_name;
	}
}

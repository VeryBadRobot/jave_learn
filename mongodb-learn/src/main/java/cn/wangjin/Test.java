package cn.wangjin;

import java.util.ArrayList;
import java.util.List;

public class Test {


	public static void main(String[] args) {

		List<Test> tests = new ArrayList<Test>();
		tests.add(null);
		for (Test t: tests)
		{
			System.out.println("11111111111111");
		}
		//tests.add(null);
		System.out.println(tests.isEmpty());
	}
}

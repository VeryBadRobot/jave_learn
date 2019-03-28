package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class LogAnalysis {

	public static void main(String[] args) throws FileNotFoundException {
		test();
//		String path = "F:\\logs\\";
//		String fileName = path+"logs.txt";
//
//		File file = new File(fileName);
//		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
//
//		HashSet<Integer> set = new HashSet<>();
//		String temp = "";
//		int count = 0;
//		try{
//			while ((temp=bufferedReader.readLine())!=null)
//			{
//				int i = temp.indexOf("swftime");
//				String test = temp.substring(i+6,i+9);
//				String[] str = test.split(",");
//				int number = Integer.parseInt(str[0]);
//
//				if (!set.contains(number))
//				{
//					set.add(number);
//				}else {
//					System.out.println(number);
//				}
//
// 				count++;
//			}
//		}catch (Exception e)
//		{
//
//		}
//		List<Integer> str = set.stream().collect(Collectors.toList());
//		Collections.sort(str);
//
//		System.out.println();
//		System.out.println("set : " + set.size());
//		System.out.println("count :  " + count);
//		System.out.println();
//
//
//		for (int d = 1; d<576;d++) {
//			if (!set.contains(d)) {
//				System.out.println("-- " + d);
//			}
//		}
	}

	public static void test() throws FileNotFoundException {
		String path = "F:\\logs\\";
		String fileName = path+"转码时间.txt";

		File file = new File(fileName);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

		List<Integer> set = new ArrayList<>();
		String temp = "";
		int count = 0;

		try{
			while ((temp=bufferedReader.readLine())!=null)
			{
				int i = temp.indexOf("swftime");
				int j = temp.indexOf("pitures");
				String test = temp.substring(i+8,i+14);
				System.out.println("swftime : " + test);

				String test2 = temp.substring(j+13,j+18);
				System.out.println("pictures : " + test2);
				try {
					int number2 = Integer.parseInt(test2.trim());
					int number = Integer.parseInt(test.trim());
					set.add(number+number2);
					count++;
				}catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}catch (Exception e)
		{
			e.printStackTrace();
		}

		int sum = 0;
		for (int i : set)
		{
			sum+=i;
		}

		double average = ((double)sum)/count;
		System.out.println("average : " + average);
		System.out.println();
		System.out.println("set : " + set.size());
		System.out.println("count :  " + count);
		System.out.println();
	}






}

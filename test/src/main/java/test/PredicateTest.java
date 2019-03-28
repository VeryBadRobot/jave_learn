package test;

import cn.strong.leke.common.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PredicateTest {


	static int num = 10000000;


	public static void main(String[] args) {

		List<ResourceTag> resourceTags = generate(num);
		List<ResourceTag> result = new ArrayList<>();

		List<ResourceTag> result2 = new ArrayList<>();


		long start1 = System.currentTimeMillis();
//		for (ResourceTag n: resourceTags)
//		{
//			if (n.getOldId().equals(200) && n.getTypeId().equals(5))
//			{
//				result.add(n);
//			}
//		}
//
//		for (ResourceTag n: resourceTags)
//		{
//			if (n.getTypeId().equals(5) && n.getOldId().equals(200))
//			{
//				result2.add(n);
//			}
//		}

		ListUtils.filter(resourceTags, n -> n.getOldId().equals(1000) && n.getParentId().equals(350));
		long end1 = System.currentTimeMillis();

		long start2 = System.currentTimeMillis();



		ListUtils.filter(resourceTags, n -> n.getParentId().equals(350) && n.getOldId().equals(1000));
		long end2 = System.currentTimeMillis();


		System.out.println(" test1 cost : " + (end1 - start1));
		System.out.println(" test2 cost : " + (end2 - start2));

	}


	static List<ResourceTag> generate(int num) {
		List<ResourceTag> result = new ArrayList<>(num);

		int i = 1;
		int count = num + 1;
		ResourceTag resourceTag = null;
		Random random = new Random();


		for (; i < count; i++) {
			resourceTag = new ResourceTag();
			resourceTag.setTagId(i);
			resourceTag.setOldId(random.nextInt(5000));
			resourceTag.setParentId(random.nextInt(500));
			resourceTag.setTypeId(random.nextInt(8));
			resourceTag.setTagName("test" + i);
			result.add(resourceTag);
		}

		return result;
	}


}

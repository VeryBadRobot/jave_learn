package test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

class ResourceTag {
	private Integer tagId;
	private String tagName;
	private Integer parentId;
	private Integer  oldId;
	private Integer  typeId;

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getOldId() {
		return oldId;
	}

	public void setOldId(Integer oldId) {
		this.oldId = oldId;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
}


public class PerformanceTest {

	public static void main(String[] args) {

		int n = 1000;

		arrTest(n);
		stackTest(n);


	}


	static void arrTest(int num) {
		long start = System.currentTimeMillis();
		List<ResourceTag> list = new ArrayList<>();
		int i = 0;
		while (i < num) {
			list.add(produce());
			i++;
		}

		for (i = list.size() - 1; i >= 0; i--) {
			list.get(i);
		}

		long end = System.currentTimeMillis();

		System.out.println("arrTest : " + (end - start));
	}


	static void stackTest(int num) {
		long start = System.currentTimeMillis();
		Stack<ResourceTag> stack = new Stack<>();

		int i = 0;
		while (i < num) {
			stack.push(produce());
			i++;
		}

		while (!stack.empty()) {
			stack.pop();
		}
		long end = System.currentTimeMillis();

		System.out.println("stackTest : " + (end - start));

	}


	static ResourceTag produce() {
		ResourceTag resourceTag = new ResourceTag();
		resourceTag.setTagId(10);
		resourceTag.setTagName("test");
		return resourceTag;
	}
}

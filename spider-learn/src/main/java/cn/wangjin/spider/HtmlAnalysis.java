package cn.wangjin.spider;

import cn.wangjin.spider.model.User;
import cn.wangjin.spider.util.ExcelUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlAnalysis {


	public static void main(String[] args) throws IOException {
		String parentPath = "C:\\Users\\123\\Documents\\转正申请";

		File[] files = getFiles(parentPath);

		String sheetTitle = "转正申请";
		String[] title = {"文件名", "部门", "档案", "姓名"};

		List<Object> userList = new ArrayList<Object>();
		for (File f : files) {
			User user = getUser(f);
			if (user!=null)
			{
				userList.add(user);
			}

		}
		byte[] result = ExcelUtil.export(sheetTitle,title,userList);

		File out = new File("C:\\Users\\123\\Documents\\转正申请.xlsx");

		FileOutputStream fileOutputStream = new FileOutputStream(out);
		fileOutputStream.write(result);
		fileOutputStream.flush();
		fileOutputStream.close();
	}

	public static File[] getFiles(String parentPath) {
		File file = new File(parentPath);

		if (!file.isDirectory()) {
			throw new RuntimeException(parentPath + "不是文件夹！");
		}

		return file.listFiles();
	}

	public static User getUser(File file) throws IOException {
		User user = new User();
		user.setFileName(file.getName());
		System.out.println(file.getName());
		/**
		 * 下面是Jsoup展现自我的平台
		 */
		//6.Jsoup解析html
		Document document = Jsoup.parse(file, "UTF-8");
		//像js一样，通过标签获取title

		Elements elements = document.getElementsByClass("td-info");
		Element deptNode = elements.first();
		if (deptNode==null)
		{
			return null;
		}
		String dept = deptNode.text();

		user.setDept(dept);
		System.out.println(dept);
		Element docNode = deptNode.child(0);
		String doc = docNode.attr("navtitle");

		user.setDoc(doc);
		System.out.println(doc);
		String name = docNode.text();
		user.setName(name);
		System.out.println(name);

		return user;
	}


}

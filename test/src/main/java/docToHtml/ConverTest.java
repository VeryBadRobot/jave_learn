package docToHtml;



public class ConverTest {
	static {
		String s = NativeOfficeConverter.class.getClassLoader().getResource("DocTOHtml.dll").getPath();
		System.load("D:\\githup\\jave_learn\\test\\src\\main\\resources\\DocTOHtml.dll");
	}

	public static void main(String[] args) {
		String file = "F:\\test\\";
		String doc = "（精校版）2019年浙江卷数学高考试题文档版（含答案）.doc";

		convertByJNI(file + doc);
	}


	public static void convertByJNI(String path) {
		NativeOfficeConverter nativeOfficeConverter = new NativeOfficeConverter();
		nativeOfficeConverter.init();

		long start = System.currentTimeMillis();

		int i = nativeOfficeConverter.docToHtmlConvert(path);

		System.out.println("cost : " + (System.currentTimeMillis() - start));
	}


	public static void convertByAspose(String path) {
//		Document doc = new Document(dataDir + "document.doc");
//
//		doc.save(dataDir + "html/Aspose_DocToHTML.html",SaveFormat.HTML); //Save the document in
	}

}

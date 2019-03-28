package word;

import java.io.*;


import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ooxml.extractor.POIXMLTextExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class JavaWord {


	/**
	 * @Title: main
	 * @Description:
	 * @param：
	 * @return void
	 * @user： wangzg
	 * @Date：2014-7-3
	 * @throws
	 */
	public static void main(String[] args) {

		readWord2003("F:\\test.doc");

    //readWord2007("F:\\1.docx");
	}



	/**
	 *
	 * @Title: readWord2003
	 * @Description:
	 * @param：
	 * @return String
	 * @user： wangzg
	 * @Date：2014-7-4
	 * @throws
	 */
	public static String readWord2003(String filePath) {
		FileInputStream fis;
		HWPFDocument doc;
		String text = null;
		try {
			File f = new File(filePath);
			fis = new FileInputStream(f);
			doc = new HWPFDocument(fis);
			Range rang = doc.getRange();
			text = rang.text();
			Paragraph p = rang.getParagraph(1);

			FileOutputStream fos = new FileOutputStream("F:\\test\\"+"txt.txt");
			fos.write(text.getBytes());
			fos.close();
			System.out.println("test");
			System.out.print(text);
			System.out.println(text);

			fis.close();
		} catch (FileNotFoundException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}

	/**
	 *
	 * @Title: readWord2007
	 * @Description:
	 * @param：
	 * @return String
	 * @user： wangzg
	 * @Date：2014-7-4
	 * @throws
	 */
	public static String readWord2007(String filePath){

		String text = null;
		try {
			OPCPackage oPCPackage = POIXMLDocument.openPackage(filePath);
			XWPFDocument xwpf = new XWPFDocument(oPCPackage);
			POIXMLTextExtractor ex = new XWPFWordExtractor(xwpf);
			text = ex.getText();

			FileOutputStream fos = new FileOutputStream("F:\\test\\"+"txt.txt");
			fos.write(text.getBytes());
			fos.close();
			System.out.println(text);
			oPCPackage.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}
}
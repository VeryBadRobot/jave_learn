package word;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

public class GetPicsDocx {
	public static void main(String[] args) {
		String path ="F:\\test.docx";
		File file = new File(path);
		try {
			FileInputStream fis = new FileInputStream(file);
			XWPFDocument document = new XWPFDocument(fis);
			XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(document);
			String text = xwpfWordExtractor.getText();
			System.out.println(text);
			List<XWPFPictureData> picList = document.getAllPictures();
			for (XWPFPictureData pic : picList) {

				System.out.println(pic.getPictureType() + file.separator + pic.suggestFileExtension()
						+file.separator+pic.getFileName());
				byte[] bytev = pic.getData();
				FileOutputStream fos = new FileOutputStream("F:\\test\\"+pic.getFileName());
				fos.write(bytev);
			}
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
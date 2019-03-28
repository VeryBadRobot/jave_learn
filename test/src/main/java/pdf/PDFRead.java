package pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PDFRead {


	public static void main(String[] args) {
		readPDF();
	}


	public static  void readPDF() {
		PDDocument helloDocument = null;
		try {
			helloDocument = PDDocument.load(new File(
					"F:\\test.pdf"));
			PDFTextStripper textStripper = new PDFTextStripper("GBK");
			System.out.println(textStripper.getText(helloDocument));

			helloDocument.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

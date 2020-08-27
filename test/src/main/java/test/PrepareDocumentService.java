package test;

import org.apache.pdfbox.pdmodel.PDDocument;

import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFSlideShowImpl;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.DefaultParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 文档文件预处理服务。
 */
@Component
public class PrepareDocumentService {

	private static final Logger logger = LoggerFactory.getLogger(PrepareDocumentService.class);

	private static final Map<String, PageReader> readers = new HashMap<>();

	private static interface PageReader {
		public Integer read(FileSource source) throws IOException;
	}

	static {
		// ppt
		readers.put("ppt", source -> {
			try (InputStream input = source.getInputStream()) {
				try (HSLFSlideShowImpl hslfSlideShow = new HSLFSlideShowImpl(input); HSLFSlideShow ppt = new HSLFSlideShow(hslfSlideShow)) {
					//good
					System.out.println("ppt HSLFSlide applicationName : " + hslfSlideShow.getSummaryInformation().getApplicationName());
					System.out.println("ppt HSLFSlide pageCount : " + ppt.getSlides().size());
					return ppt.getSlides().size();
				} catch (OfficeXmlFileException e) {
					logger.info("file ext failed.");
				}
			}
			// compat
			try (InputStream input = source.getInputStream()) {
				try (XMLSlideShow pptx = new XMLSlideShow(input)) {
					System.out.println("ppt XMLSlide applicationName : " + pptx.getProperties().getExtendedProperties().getApplication());
					System.out.println("ppt XMLSlide pageCount : " + pptx.getSlides().size());
					return pptx.getSlides().size();
				}
			}
		});
		// pptx
		readers.put("pptx", source -> {
			try (InputStream input = source.getInputStream()) {
				try (XMLSlideShow pptx = new XMLSlideShow(input)) {
					//good
					System.out.println("pptX XMLSlide applicationName : " + pptx.getProperties().getExtendedProperties().getApplication());
					System.out.println("pptX XMLSlide pageCount : " + pptx.getSlides().size());
					return pptx.getSlides().size();
				} catch (OLE2NotOfficeXmlFileException e) {
					logger.info("file ext failed.");
				}
			}
			// compat
			try (InputStream input = source.getInputStream()) {
				try (HSLFSlideShowImpl hslfSlideShow = new HSLFSlideShowImpl(input); HSLFSlideShow ppt = new HSLFSlideShow(hslfSlideShow)) {
					System.out.println("pptX HSLFSlide applicationName : " + hslfSlideShow.getSummaryInformation().getApplicationName());
					System.out.println("pptX HSLFSlide pageCount : " + ppt.getSlides().size());
					return ppt.getSlides().size();
				}
			}
		});
		// doc
		readers.put("doc", source -> {
			try (InputStream input = source.getInputStream()) {
				try (WordExtractor doc = new WordExtractor(input)) {
					System.out.println("doc wordExtractor applicationName : "+ doc.getSummaryInformation().getApplicationName());
					return doc.getSummaryInformation().getPageCount();
				} catch (OfficeXmlFileException e) {
					logger.info("file ext failed.");
				}
			}
			// compat
			try (InputStream input = source.getInputStream()) {
				try (XWPFDocument docx = new XWPFDocument(input)) {
					System.out.println("doc XWPF applicationName : "+docx.getProperties().getExtendedProperties().getUnderlyingProperties().getApplication());
					return docx.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
				}
			}
		});
		// docx
		readers.put("docx", source -> {
			try (InputStream input = source.getInputStream()) {
				try (XWPFDocument docx = new XWPFDocument(input)) {
					XWPFWordExtractor extractor = new XWPFWordExtractor(docx);
					System.out.println("docx xwp 1 application : " +docx.getProperties().getExtendedProperties().getApplication());
					System.out.println("docx xwp 2 application : " + docx.getProperties().getExtendedProperties().getUnderlyingProperties().getApplication());

					return extractor.getExtendedProperties().getPages();
					//return docx.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
				} catch (OLE2NotOfficeXmlFileException e) {
					logger.info("file ext failed.");
				}
			}
			// compat
			try (InputStream input = source.getInputStream()) {
				try (WordExtractor doc = new WordExtractor(input)) {
					System.out.println("test");
					return doc.getSummaryInformation().getPageCount();
				}
			}
		});
		// pdf
		readers.put("pdf", source -> {
			try (InputStream input = source.getInputStream()) {
				try (PDDocument pdf = PDDocument.load(input)) {
					System.out.println("pdf pageCount : " + pdf.getNumberOfPages());

					return pdf.getNumberOfPages();
				}
			}
		});
		// xls
		readers.put("xls", source -> {
			HSSFWorkbook hs = new HSSFWorkbook(source.getInputStream());
			//good
			System.out.println("xls HSSFWorkbook applicationName : " + hs.getSummaryInformation().getApplicationName());
			System.out.println("xls HSSFWorkbook pageCount : " + hs.getNumberOfSheets());
			return hs.getNumberOfSheets();
		});
		// xlsx
		readers.put("xlsx", source -> {


			// compat
			try {
				HSSFWorkbook hs = new HSSFWorkbook(source.getInputStream());
				System.out.println("xlsx HSSFWorkbook applicationName : " + hs.getSummaryInformation().getApplicationName());
				System.out.println("xlsx HSSFWorkbook pageCount : " + hs.getNumberOfSheets());
				return hs.getNumberOfSheets();
			} catch (Exception e) {
				XSSFWorkbook xs = new XSSFWorkbook(source.getInputStream());
				//good
				System.out.println("xlsx XSSFWorkbook applicationName : " + xs.getProperties().getExtendedProperties().getApplication());
				System.out.println("xlsx XSSFWorkbook pageCount : " + xs.getNumberOfSheets());
				return xs.getNumberOfSheets();
			}

		});
	}

	public static void execute(FileSource source) {
		/**
		 * 获取文件扩展名
		 */
		String ext = source.getExt().toLowerCase();
		/**
		 * 根据扩展名，找到对应的文件读取器
		 */
		PageReader reader = readers.get(ext);
		if (reader == null) {
			throw new RuntimeException("文件类型不支持读取页数");
		}
		Integer pageCount = null;
		try {

			pageCount = reader.read(source);

			System.out.println("pageCount :'" + pageCount + "'");
		} catch (IOException e) {
			throw new RuntimeException("file read error.", e);
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException, TikaException, SAXException {
		String path = "C:\\Users\\123\\Documents\\Tencent Files\\516671256\\FileRecv";
		String name1 = path + "\\" + "office-file.xlsx";
		String name2 = path + "\\" + "wps-file.xlsx";

		String name6 = path + "\\" + "施强CICD.pptx";
		//docx读取错误
		String name3 = path + "\\" + "Harry PotterWPS.docx";
		String name4 = path + "\\" + "operatingsystem.pdf";
		String name5 = path + "\\" + "人教版生物必修1： 全册总复习课件 (1)(共546张PPT).pptx";

		String name7 = path + "\\" + "单词学习之词根mand.ppt";
		String name8 = path + "\\" + "单词学习之词根mand-wps.pptx";
		String name9 = path + "\\" + "5.18-二大区-合作学校全网跟进表.xlsx";
		String name10 = path + "\\" + "人口与人种2.pptx";
		String name11 = path + "\\" + "赵州桥.pptx";
		String name12 = path + "\\" + "1c.docx";
		String name13 = path + "\\" + "中国现有化学物质名录2013年版(2).pdf";
		String name14 = path+"\\"+"OFFICE-file.ppt";
		String name15 = path+"\\"+"WPS-file.ppt";
		String name16 = path+"\\"+"none.ppt";
		String name17 = path+"\\"+"office-file.pptx";
		String name18 = path+"\\"+"office-file.doc";
		String name19 = path+"\\"+"wps-file.doc";
		String name20 = path+"\\"+"none-file.xls";
		String name21 = path+"\\"+"wps-file.xls";
		String name22 = path+"\\"+"office-file.xls";


		FileSource fileSource = new FileSystemSource(name4);
		Parser parser = new OOXMLParser();
		((OOXMLParser) parser).setUseSAXDocxExtractor(true);
		((OOXMLParser) parser).setIncludeDeletedContent(true);
		((OOXMLParser) parser).setIncludeMoveFromContent(true);



//
//		ContentHandler handler = new BodyContentHandler();
//
//		Metadata metadata = new Metadata();
//		ParseContext context = new ParseContext();
//
//		parser.parse(fileSource.getInputStream(),handler,metadata,context);

		//System.out.println(metadata);

		try {
			execute(fileSource);
		} catch (Exception e) {
			System.out.println(e);
		}

		//System.gc();

		TimeUnit.MINUTES.sleep(10);

	}

}

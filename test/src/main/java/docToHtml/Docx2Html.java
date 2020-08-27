package docToHtml;



import org.docx4j.Docx4J;
import org.docx4j.Docx4jProperties;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.File;
import java.io.FileOutputStream;


public class Docx2Html  {

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        String inputfilepath = "F:\\test\\（精校版）2019年全国卷Ⅰ理数高考试题文档版（含答案）.docx";
        boolean nestLists = false;

        WordprocessingMLPackage wordMLPackage = Docx4J
                .load(new File(inputfilepath));

        HTMLSettings htmlSettings = Docx4J.createHTMLSettings();

        htmlSettings.setImageDirPath(inputfilepath + "_files");
        htmlSettings.setImageTargetUri(inputfilepath.substring(inputfilepath.lastIndexOf("/") + 1) + "_files");
        htmlSettings.setWmlPackage(wordMLPackage);

        String userCSS = null;
        if (nestLists) {
            userCSS = "html, body, div, span, h1, h2, h3, h4, h5, h6, p, a, img,  table, caption, tbody, tfoot, thead, tr, th, td "
                    + "{ margin: 0; padding: 0; border: 0;}" + "body {line-height: 1;} ";
        } else {
            userCSS = "html, body, div, span, h1, h2, h3, h4, h5, h6, p, a, img,  ol, ul, li, table, caption, tbody, tfoot, thead, tr, th, td "
                    + "{ margin: 0; padding: 0; border: 0;}" + "body {line-height: 1;} ";

        }
        htmlSettings.setUserCSS(userCSS);

        Docx4jProperties.setProperty("docx4j.Convert.Out.HTML.OutputMethodXML", true);

        Docx4J.toHTML(htmlSettings, new FileOutputStream(new File("F:\\test\\（精校版）2019年全国卷Ⅰ理数高考试题文档版（含答案）.html")),
                Docx4J.FLAG_EXPORT_PREFER_XSL);

        if (wordMLPackage.getMainDocumentPart().getFontTablePart() != null) {
            wordMLPackage.getMainDocumentPart().getFontTablePart().deleteEmbeddedFontTempFiles();
        }
        htmlSettings = null;
        wordMLPackage = null;

        long cost = System.currentTimeMillis()-start;
        System.out.println("cost: " + cost);
    }
}
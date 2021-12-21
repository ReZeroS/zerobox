package club.qqtim.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class PDFGenerator {


    public static <T> ByteArrayInputStream exportPdf(List<T> elements, List<String> configList) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);
            document.open();

            // Add Text to PDF file ->
            Font font = FontFactory.getFont(FontFactory.COURIER, 14,
                                          BaseColor.BLACK);
            Paragraph para = new Paragraph("Employee Table", font);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(3);
            // Add PDF Table Header ->
            Stream.of("ID", "First Name", "Last Name").forEach(headerTitle ->
                                     {
                PdfPCell header = new PdfPCell();
                Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setBorderWidth(2);
                header.setPhrase(new Phrase(headerTitle, headFont));
                table.addCell(header);
            });

            for (T element : elements) {
                for (String config : configList) {
                    PdfPCell idCell = new PdfPCell(
                            new Phrase(BeanUtils.getProperty(element, config)));
                    idCell.setPaddingLeft(4);
                    idCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(idCell);
                }
            }
            document.add(table);

            document.close();
        } catch (Exception e) {
            log.error("export pdf error", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}

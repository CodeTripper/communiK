package in.codetripper.communik.messagegenerator;

import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class FlyingSaucerPdfGenerator implements PdfGenerator {
    @Override
    public String generatePdf(String templateId, Object notificationMessage) throws MessageGenerationException {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("message.pdf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(templateId);
        renderer.layout();
        renderer.createPDF(outputStream);
        try {
            Objects.requireNonNull(outputStream).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

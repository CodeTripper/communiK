package in.codetripper.communik.messagegenerator;

public interface HtmlGenerator<T> {
    String generateHtml(String templateId, T notificationMessage) throws MessageGenerationException;
}

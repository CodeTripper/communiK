package in.codetripper.communik.messagegenerator;


public interface MessageGenerator<T> {

    String generateMessage(String template, T notificationMessage);
}

package in.codetripper.communik.messagegenerator;

public class MessageGenerationException extends Exception {
    public MessageGenerationException(String s) {
        // Call constructor of parent Exception
        super(s);
    }

    public MessageGenerationException(String s, Exception e) {
        // Call constructor of parent Exception
        super(s, e);
    }

}
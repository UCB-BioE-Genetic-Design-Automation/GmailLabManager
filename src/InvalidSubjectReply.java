/**
 * A reply message to users who send emails with invalid subjects.
 */
public class InvalidSubjectReply implements ReplyMessage {

    public String getName() {
        return "Invalid Subject";
    }

    public String getMessage(String request) {
        return "The subject '" + request + "' is an invalid subject. Please " +
                "resend your request with a valid subject.";
    }

}

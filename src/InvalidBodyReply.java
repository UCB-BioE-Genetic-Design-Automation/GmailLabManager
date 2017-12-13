/**
 * A reply message to users who send emails with invalid bodies.
 */
public class InvalidBodyReply implements ReplyMessage {

    @Override
    public String getName() {
        return "Invalid Body";
    }

    @Override
    public String getMessage(String request) {
        return "The body of the email you sent me with subject line '" +
                request + "' is invalid. Please resend your request with" +
                " a valid body.";
    }
}

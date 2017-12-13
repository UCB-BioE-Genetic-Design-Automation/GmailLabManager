/**
 *
 */
public class ConfirmationReply implements ReplyMessage {

    @Override
    public String getName() {
        return "Confirmation";
    }

    @Override
    public String getMessage(String request) {
        return "Your request, '" + request + "' has been processed " +
                "successfully.";
    }
}

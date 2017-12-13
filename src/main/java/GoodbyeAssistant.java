import com.google.api.services.gmail.Gmail;

/**
 * A (sample) Lab Manager Assistant who handles emails with subject like
 * "Goodbye".
 */
public class GoodbyeAssistant implements Assistant {

    @Override
    public boolean validate(Email email) {

        return (EmailUtils.getPlainTextBody(email)).equals("Goodbye forever.");
    }

    @Override
    public void process(Gmail service, String userId, Email email) {
        boolean success = true;
        if (success) {
            ReplyMessage reply = new ConfirmationReply();
            EmailUtils.sendReply(service, userId, reply, email);
            //EmailUtils.markRead(service, userId, email);
        }

    }
}




import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Control center for the Lab Manager.
 */
public class Main {

    public static String me = "lab123545@gmail.com";

    /**Scans through the lab admin email inbox, finding new unread emails and
     * routing to validating and processing functions. */
    public static void main(String[] args) throws IOException {

        // Create a Gmail service object.
        Gmail service = GmailService.getGmailService();

        // Scan inbox for all unread messages.
        List<String> labels = new ArrayList<>();
        String user = "me";
        labels.add("INBOX");
        labels.add("UNREAD");
        ListMessagesResponse response = service.users().messages().list
                ("me").setLabelIds(labels).execute();
        List<Message> msgList = new ArrayList<>();
        while (response.getMessages() != null) {
            msgList.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().messages().list(user).setLabelIds
                        (labels).setPageToken(pageToken).execute();
            } else {
                break;
            }
        }

        // Create list of corresponding Email objects.
        ArrayList<Email> newEmails = new ArrayList<>();
        for (Message msg : msgList) {
            String mssgID = msg.getId();
            Message message = service.users().messages().get(user, mssgID)
                    .execute();
            Email email = new Email(message);
            newEmails.add(email);
        }


        // Sample mapping of subjects to Assistant types
        HashMap<String, Assistant> assistants = new HashMap<>();
        HelloAssistant helloAss= new HelloAssistant();
        GoodbyeAssistant goodbyeAss = new GoodbyeAssistant();
        assistants.put("Hello", helloAss);
        assistants.put("Goodbye", goodbyeAss);

        // For every email, verify validity of subject and body formatting.
        // If valid, process the email. Otherwise, send reply message back to
        // original sender.
        Assistant assistant;
        for (Email email : newEmails) {
            String subject = email.getSubject();
            switch(subject) {
                case ("Hello"):
                    assistant = assistants.get("Hello");
                    if (assistant.validate(email)) {
                        assistant.process(service, user, email);
                    } else {
                        ReplyMessage reply = new InvalidBodyReply();
                        EmailUtils.sendReply(service, user, reply, email);
                    }
                    continue;
                case("Goodbye"):
                    assistant = assistants.get("Goodbye");
                    if (assistant.validate(email)) {
                        assistant.process(service, user, email);
                    } else {
                        ReplyMessage reply = new InvalidBodyReply();
                        EmailUtils.sendReply(service, user, reply, email);
                    }
                    continue;
                default:
                    ReplyMessage reply = new InvalidSubjectReply();
                    EmailUtils.sendReply(service, user, reply, email);
            }
        }
    }
}


import com.google.api.client.util.Base64;
import com.google.api.client.util.StringUtils;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.ModifyMessageRequest;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * A utility class for performing various email actions.
 * Several of these methods were taken from sample code provided by the Gmail
 * API documentation.
 * See https://developers.google.com/gmail/api/v1/reference/ for more details.
 */
public class EmailUtils {

    /** Sends an email using SERVICE from USERID with REPLY's message to the
     * sender of EMAIL. */
    public static void sendReply(Gmail service, String userId, ReplyMessage
            reply, Email email) {
        try {

            MimeMessage emailContent = createEmail(email.getFrom(), Main.me,
                    reply.getName(), reply.getMessage(email.getSubject()));
            sendMessage(service, userId, emailContent);
        } catch (Exception error) {
            System.out.println("Failed to send reply. ");
        }
    }
    /** Sends an email using SERVICE from USERID, given an EMAIL. */
    public static void sendMessage(Gmail service, String userId, MimeMessage
            email) throws MessagingException, IOException {
        Message message = createMessageWithEmail(email);
        message = service.users().messages().send(userId, message).execute();
//        System.out.println("Message id: " + message.getId());
//        System.out.println(message.toPrettyString());
    }

    /** Creates a Gmail API Message object given a EMAILCONTENT. */
    public static Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    /** Creates a MimeMessage given TO, FROM, SUBJECT and BODYTEXT. */
    public static MimeMessage createEmail(String to,
                                          String from,
                                          String subject,
                                          String bodyText)
            throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    /** Retrieves the plain test body of EMAIL.  */
    public static String getPlainTextBody(Email email) {
        String body = StringUtils.newStringUtf8(Base64.decodeBase64(
                email.getPayload().getParts().get(0).getBody().getData()));
        return body.replace("\r", "")
                .replace("\n", "");
    }

    /** Modifies a message with id MSGID in USERID's inbox using SERVICE.
     * Modifications are specified by LABLESTOAD and LABELSTOREMOVE. */
    public static void modifyMessage(Gmail service, String userId, String msgId,
                                     List<String> labelsToAdd, List<String>
                                             labelsToRemove) throws IOException {
        ModifyMessageRequest mods = new ModifyMessageRequest().setAddLabelIds(labelsToAdd)
                .setRemoveLabelIds(labelsToRemove);
        service.users().messages().modify(userId, msgId, mods).execute();
    }
//     /** Marks EMAIL as unread in USERID's inbox using SERVICE.  */
//    public static void markRead(Gmail service, String userId, Email email) {
//        ArrayList<String> labelsToAdd = new ArrayList<>();
//        ArrayList<String> labelsToRemove = new ArrayList<>();
//        labelsToAdd.add("READ");
//        labelsToRemove.add("UNREAD");
//        try {
//            modifyMessage(service, userId, email.getMsgId(), labelsToAdd, labelsToRemove);
//        } catch (IOException error) {
//            System.out.println("Failed to modify message.");
//        }
//    }

}

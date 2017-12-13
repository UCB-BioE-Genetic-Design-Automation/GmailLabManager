import com.google.api.client.util.Base64;
import com.google.api.client.util.StringUtils;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;

/**
 * A convenient wrapper for the Gmail API's Message object.
 */

public class Email {

    /** Constructs an email out of FROM, TO, SUBJECT and BODY. */
    public Email(String from, String to, String subject, String body) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    /** Construct an email from a Gmail API Message object. */
    public Email(Message message) {
        payload = message.getPayload();
        msgId = message.getId();
        parseMessage(payload);
    }

    /** Returns my subject. */
    public String getSubject() {
        return subject;
    }

    /** Returns my recipient. */
    public String getTo() {
        return to;
    }

    /** Returns my sender. */
    public String getFrom() {
        return from;
    }

    /** Returns my message ID. */
    public String getMsgId() {
        return msgId;
    }
    /** Returns my body.  */
    public String getBody() {
        return body;
    }

    /** Returns my paylod.  */
    public MessagePart getPayload() {
        return payload;
    }

    /** Parses my Message's PAYLOAD and populates my attributes. */
    private void parseMessage(MessagePart payload) {
        for (MessagePartHeader h : payload.getHeaders()) {
            if (h.getName().equals("Subject")) {
                subject = h.getValue();
            }
            if (h.getName().equals("To")) {
                to = h.getValue();
            }
            if (h.getName().equals("From")) {
                from = h.getValue();
            }
        }
        body = StringUtils.newStringUtf8(Base64.decodeBase64(payload.getParts()
                .get(0).getBody().getData()));
    }

    /** The payload attribute of the Gmail API Message object.
     * See https://developers.google.com/gmail/api/v1/reference/users/messages
     * for more details. */
    private MessagePart payload;

    /** The ID of an email. */
    private String msgId;

    /** The sender of an email. */
    private String from;
    /** The recipient of an email. */
    private String to;
    /** The subject line of an email header.*/
    private String subject;
    /** The content of an email, in bytes.*/
    private String body;


}

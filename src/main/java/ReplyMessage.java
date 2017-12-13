/**
 * A specific type of reply message that Lab Manager would send back to users.
 */
public interface ReplyMessage {

    /** Returns type of reply message. Used as the subject like in reply
     * emails.*/
    public String getName();

    /** Returns my particular message, which incorporates the name of
     * the REQUEST I am replying to.
     *  */
    public String getMessage(String request);



}

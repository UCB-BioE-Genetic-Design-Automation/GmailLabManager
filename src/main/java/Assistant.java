import com.google.api.services.gmail.Gmail;

/**
 * The Assitants of Lab Manager validate and process a particular type of lab
 * task.
 */
public interface Assistant {

    /** Returns true iff the body of EMAIL is formatted correctly. */
    boolean validate(Email email);

    /** Processes the request specified by the subject lone of EMAIL.
     * If a processes is successful, a Confirmation Reply is sent to the
     * sender of EMAIL. */
    public void process(Gmail service, String userId, Email email);

}

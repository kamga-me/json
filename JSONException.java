package json;

/**
* Base exception class for reporting JSON related errors.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public class JSONException extends RuntimeException {

	/**The class's serial version UID. */
	public static final long serialVersionUID = 0x021DB63101B6928AL;
	
	public JSONException() {
		super();
	}
	
	public JSONException(final java.lang.String errMsg) {
		super(errMsg);
	}
	
	public JSONException(final java.lang.String errMsg, java.lang.Throwable t) {
		super(errMsg, t);
	}

}
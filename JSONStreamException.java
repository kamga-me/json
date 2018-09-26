package json.stream;

/**
* Special exception class for reporting JSON stream related errors.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public class JSONStreamException extends json.JSONException {

	/**The class's serial version UID. */
	public static final long serialVersionUID = 0x021DB63EA1B6928AL;
	
	public JSONStreamException() {
		super();
	}
	
	public JSONStreamException(final java.lang.String errMsg) {
		super(errMsg);
	}
	
	public JSONStreamException(final java.lang.String errMsg, java.lang.Throwable t) {
		super(errMsg, t);
	}

}
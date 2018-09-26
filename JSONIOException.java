package json;

/**
* Exception class for reporting I/O errors in relation to JSON.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public class JSONIOException extends JSONException {

	/**The class's serial version UID. */
	public static final long serialVersionUID = 0x022719F7F1B6928AL;
	
	/**
	* Constructor.<br>
	*/
	public JSONIOException() {
		super();
	}
	/**
	* Constructor.<br>
	*/
	public JSONIOException(final java.lang.String errMsg) {
		super(errMsg);
	}
	/**
	* Constructor.<br>
	*/
	public JSONIOException(final java.lang.String errMsg, java.lang.Throwable t) {
		super(errMsg, t);
	}

}
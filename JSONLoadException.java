package json;

/**
* Exception class for reporting JSON document load related errors.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public class JSONLoadException extends JSONException {

	/**The class's serial version UID. */
	public static final long serialVersionUID = 0x0227106AD1B6928AL;
	
	/**
	* Default constructor.<br>
	*/
	public JSONLoadException() {
		super();
	}
	/**
	* Constructor.<br>
	*/
	public JSONLoadException(final java.lang.String errMsg) {
		super(errMsg);
	}
	/**
	* Constructor.<br>
	*/
	public JSONLoadException(final java.lang.String errMsg, java.lang.Throwable t) {
		super(errMsg, t);
	}

}
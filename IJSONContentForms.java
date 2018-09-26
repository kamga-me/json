package json;

/**
* Interface with integer number constants for the JSON document content forms.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public interface IJSONContentForms {

	public static final byte JSON_OBJECT = 1;
	
	public static final byte JSON_ARRAY = 2;
	
	public static final byte JSON_SCALAR = 3;
	
	public static final byte UNSPECIFIED_JSON_CONTENT_FORM = 0;
	
}
package json.stream;

/**
* Interface with integer number constant declarations for <code>JSON</code> event types.<br>
*
* <br><br>References</b>: <br>
* <li><a href="https://datatracker.ietf.org/doc/rfc7158/?include_text=1">The JavaScript Object Notation (JSON) Data Interchange Format - RFC7158</a>
* <li><a href="http://rfc7159.net/rfc7159">The JavaScript Object Notation (JSON) Data Interchange Format - RFC7159</a>
* <li><a href="http://www.json.org/">Introducing JSON</a>
* <li><a href="http://en.wikipedia.org/wiki/JSON">JSON - wikipedia page</a>
* <li><a href="http://json-schema.org/documentation.html">The home of JSON schema</a>
* <br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public interface IJSONSreamEventTypes {

	/**
	* Constant for signalling that the read of the <code>JSON</code> document/data has not started yet.
	*/
	public static final byte DOC_READ_NOT_STARTED = -127;
	/**
	* Constant for <code>empty-json-stream</code> event type.
	*/
	public static final byte EMPTY_JSON_STREAM = -1;
	
	/**
	* Constant for <code>object-start</code> event type.<br>
	* A {@link #OBJECT_START OBJECT_START} is either a root object or a child/descendant item of the root {@link #ARRAY_START ARRAY_START}.<br>
	* A {@link #OBJECT_START OBJECT_START} cannot fire underneath a {@link #OBJECT_START OBJECT_START}.<br>
	* <b>NOTE</b>: there can only be one root node ({@link #ARRAY_START} or {@code OBJECT_START}) per json document.<br>
	*/
	public static final byte OBJECT_START = 1;
	/**
	* Constant for <code>object-array-start</code> event type.<br>
	* A {@link #ARRAY_START ARRAY_START} can only fire at root level or just below a {@link #ARRAY_START ARRAY_START}.<br>
	* <b>NOTE</b>: there can only be one root node ({@link #OBJECT_START} or {@code ARRAY_START}) per json document.<br>
	*/
	public static final byte ARRAY_START = 2;
	/**
	* Constant for <code>array-simple-item</code> event type.<br>
	* A {@link #ARRAY_SIMPLE_ITEM ARRAY_SIMPLE_ITEM} is bound to a {@link #ARRAY_START ARRAY_START}.<br>
	*/
	public static final byte ARRAY_SIMPLE_ITEM = 3;
	/**
	* Constant for <code>property-name</code> event type.<br>
	* A {@link #PROPERTY_NAME PROPERTY_NAME} is followed by a {@link #PROPERTY_VALUE PROPERTY_VALUE}, a {@link #PROPERTY_OBJECT_START PROPERTY_OBJECT_START}, or a {@link #PROPERTY_ARRAY_START PROPERTY_ARRAY_START}.
	*/
	public static final byte PROPERTY_NAME = 4;
	/**
	* Constant for <code>property-value</code> event type.
	*/
	public static final byte PROPERTY_VALUE = 5;
	/**
	* Constant for property <code>object-value-start</code> event type.
	*/
	public static final byte PROPERTY_OBJECT_START = 6;
	/**
	* Constant for property <code>object-value-end</code> event type.
	*/
	public static final byte PROPERTY_OBJECT_END = 7;
	/**
	* Constant for property <code>array-value-start</code> event type.<br>
	*/
	public static final byte PROPERTY_ARRAY_START = 8;
	/**
	* Constant for property <code>array-value-simple-item</code> event type.<br>
	* <b>NOTE</b>: some implementations may choose only use {@link #ARRAY_SIMPLE_ITEM ARRAY_SIMPLE_ITEM} for all the cases of array items that are actually simple values.<br>
	* a {@link #PROPERTY_ARRAY_SIMPLE_ITEM PROPERTY_ARRAY_SIMPLE_ITEM} is bound to a {@link #PROPERTY_ARRAY_START PROPERTY_ARRAY_START} or a {@link #PROPERTY_ARRAY_ARR_ITM_START PPROPERTY_ARRAY_ARR_ITM_START}.<br>
	*/
	public static final byte PROPERTY_ARRAY_SIMPLE_ITEM = 9;
	/**
	* Constant for property <code>array-value-object-item-start</code> event type.<br>
	* A {@link #PROPERTY_ARRAY_OBJ_ITM_START PROPERTY_ARRAY_OBJ_ITM_START} can only be nested into an event of type {@link #PROPERTY_ARRAY_START PROPERTY_ARRAY_START} or {@link #PROPERTY_ARRAY_ARR_ITM_START PROPERTY_ARRAY_ARR_ITM_START}.
	*/
	public static final byte PROPERTY_ARRAY_OBJ_ITM_START = 10;
	/**
	* Constant for property <code>array-value-object-item-end</code> event type.
	*/
	public static final byte PROPERTY_ARRAY_OBJ_ITM_END = 11;
	/**
	* Constant for property <code>array-value-subarray-item-start</code> event type.<br>
	* A {@link #PROPERTY_ARRAY_ARR_ITM_START PROPERTY_ARRAY_ARR_ITM_START} can only be nested into an event of type {@link #PROPERTY_ARRAY_START PROPERTY_ARRAY_START} or {@link #PROPERTY_ARRAY_ARR_ITM_START PROPERTY_ARRAY_ARR_ITM_START}.
	*/
	public static final byte PROPERTY_ARRAY_ARR_ITM_START = 12;
	/**
	* Constant for property <code>array-value-subarray-item-end</code> event type.
	*/
	public static final byte PROPERTY_ARRAY_ARR_ITM_END = 13;
	/**
	* Constant for property <code>array-value-end</code> event type.
	*/
	public static final byte PROPERTY_ARRAY_END = 14;
	/**
	* Constant for <code>object-array-start</code> event type.<br>
	* <b>NOTE</b>: there can only be one root end ({@link #OBJECT_END} or {@code ARRAY_END}) per json document.<br>
	*/
	public static final byte ARRAY_END = 15;
	/**
	* Constant for <code>object-end</code> event type.<br>
	* <b>NOTE</b>: there can only be one root end ({@link #ARRAY_END} or {@code OBJECT_END}) per json document.<br>
	*/
	public static final byte OBJECT_END = 16;
	/**
	* Constant for <code>end-of-stream</code> event type.
	*/
	public static final byte END_OF_STREAM = 127;
	
	/**
	* The maximum number of an event type that is neither {@link #DOC_READ_NOT_STARTED DOC_READ_NOT_STARTED} nor {@link #EMPTY_JSON_STREAM EMPTY_JSON_STREAM} nor {@link #END_OF_STREAM END_OF_STREAM}
	*/
	public static final byte MAX_JSON_EVENT_TYPE_NUMBER = OBJECT_END;
	
	/**
	* Denotes a JSON document whose content is a mere single scalar.<br>
	*/
	public static final byte SCALAR = 0; //since 2018-01-01
	/**
	* Indicates that an event type does not have a corresponding peer.<br>
	*/
	public static final byte NO_PEER_EVENT_TYPE = -128;


}
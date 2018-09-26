package json.stream;

/**
* Utility class with istatic methods that are handy in dealing with <code>JSON</code> event types.<br>
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
public final class JSONSreamEventTypes implements IJSONSreamEventTypes, json.IJSONContentForms {

	private static final byte[] PEER_EVENT_TYPES = new byte[MAX_JSON_EVENT_TYPE_NUMBER + 1];
	static {
		final byte[] a = PEER_EVENT_TYPES;
		
		for (int i=0;i<PEER_EVENT_TYPES.length;i++)
		{
			a[i] = NO_PEER_EVENT_TYPE;
		}
		
		a[OBJECT_START] = OBJECT_END;
		a[OBJECT_END] = OBJECT_START;
		a[ARRAY_START] = ARRAY_END;
		a[ARRAY_END] = ARRAY_START;
		
		a[PROPERTY_OBJECT_START] = PROPERTY_OBJECT_END;
		a[PROPERTY_OBJECT_END] = PROPERTY_OBJECT_START;
		a[PROPERTY_ARRAY_START] = PROPERTY_ARRAY_END;
		a[PROPERTY_ARRAY_END] = PROPERTY_ARRAY_START;
		
		a[PROPERTY_ARRAY_OBJ_ITM_START] = PROPERTY_ARRAY_OBJ_ITM_END;
		a[PROPERTY_ARRAY_OBJ_ITM_END] = PROPERTY_ARRAY_OBJ_ITM_START;
		a[PROPERTY_ARRAY_ARR_ITM_START] = PROPERTY_ARRAY_ARR_ITM_END;
		a[PROPERTY_ARRAY_ARR_ITM_END] = PROPERTY_ARRAY_ARR_ITM_START;
		
	}
	
	private static final byte[] CONTENT_FORMS = new byte[MAX_JSON_EVENT_TYPE_NUMBER + 1];
	static {
		final byte[] a = CONTENT_FORMS;
		a[OBJECT_START] = JSON_OBJECT;
		a[ARRAY_START] = JSON_ARRAY;
		
		a[PROPERTY_OBJECT_START] = JSON_OBJECT;
		a[PROPERTY_ARRAY_START] = JSON_ARRAY;
		
		a[PROPERTY_ARRAY_OBJ_ITM_START] = JSON_OBJECT;
		a[PROPERTY_ARRAY_ARR_ITM_START] = JSON_ARRAY;
		
		a[ARRAY_SIMPLE_ITEM] = JSON_SCALAR;
		a[PROPERTY_VALUE] = JSON_SCALAR;
		a[PROPERTY_ARRAY_SIMPLE_ITEM] = JSON_SCALAR;
		a[SCALAR] = JSON_SCALAR;
		
	}
	/**
	* Gets the number of the event type that is peer to the indicated content-related event type.
	* @param eventType the number of the content-related event type of interest - valid values are those in range <code>0..{@link #MAX_JSON_EVENT_TYPE_NUMBER MAX_JSON_EVENT_TYPE_NUMBER}
	*
	* @see #NO_PEER_EVENT_TYPE NO_PEER_EVENT_TYPE
	*/
	public static final byte get_PeerEventType(final byte eventType) {
		return PEER_EVENT_TYPES[eventType];
	}
	/**
	* Gets the content form of the event type of the indicated content-related event type.
	* @param eventType the number of the content-related event type of interest - valid values are those in range <code>0..{@link #MAX_JSON_EVENT_TYPE_NUMBER MAX_JSON_EVENT_TYPE_NUMBER}
	*/
	public static final byte get_ContentForm(final byte eventType) {
		return CONTENT_FORMS[eventType];
	}

	/**
	* Tells if the JSON stream event of the indicated type is an structure ending event.<br>
	*/
	public static final boolean isEndingEvent(final byte eventType) {
		switch(eventType)
		{
		case ARRAY_END:
		case OBJECT_END:
		case PROPERTY_ARRAY_END:
		case PROPERTY_OBJECT_END:
		case PROPERTY_ARRAY_ARR_ITM_END:
		case PROPERTY_ARRAY_OBJ_ITM_END: 
			return true;
		}
		return false;
	}

}
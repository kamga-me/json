package json;

import core.collection.IList;
import string.String;

/**
* Base class for providing support for JSON objects/nodes that are lists of nodes.<br>
* 
* @param <T> the class of the top/root element of the JDON document - must be a sub-class of {@link JSONObject JSONObject}, {@link JSONObjectList JSONObjectList}, {@link core.Value Value}, {@link core.Array Array} or {@link core.Thing Thing} with interface {@link core.collection.IList IList} implemented.
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public abstract class JSONObjectList<T extends JSONObject> extends JSONList<T> {

	/**
	* Constructor.<br>
	*/
	protected JSONObjectList() {
		super();
	}
	
	/**
	* {@inheritDoc}
	* @return {@link #yes yes}
	*/
	public final byte onlyHasNodeItems() {return yes; }
	
	/**
	* {@inheritDoc}
	* @return <code>true</code>
	*/
	public final boolean isJSONObjectList() {return true; }
	/**
	* {@inheritDoc}
	* @return <code>this</code>
	*/
	public final JSONObjectList<T> asJSONObjectList() {return this; }
	
}
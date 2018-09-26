package json;

import string.ICharSequence;
import string.String;
import core.IValueTypes;

/**
* Class for representing JSON documents.<br>
* This class can be seen as useless, but at least it serves to wrap all the sort of possible top/toot elements.<br>
*
* @param <E> the class of the top/root element of the JDON document - must be a sub-class of {@link JSONObject JSONObject}, {@link JSONList JSONList}, {@link core.Value Value}, {@link core.Array Array} or {@link core.Thing Thing} with interface {@link core.collection.IList IList} implemented.
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public class JSONDocument<E extends core.Thing> extends JSONNode { //since 2018-04-22

	/**The class's serial version UID. */
	public static final long serialVersionUID = 0x0226F31F61B6928AL;
	
	protected E element;
	
	/**
	* Constructor.<br>
	*/
	public JSONDocument(E element) {
		super();
		this.element = element;
	}
	/**
	* Default constructor.<br>
	*/
	protected JSONDocument() {
		this(null);
	}
	/**
	* Gets the top/root element of the document.<br> 
	* The top/root element can be either a JSONObject or an array object (a JSONList or a {@link core.Array Array}) or a scalar.<br>
	*/
	public E getElement() {return element; }
	
	/**
	* {@inheritDoc}
	*/
	public final byte getObjectType() {
		return JSON_DOCUMENT;
	}
	
	/**
	* {@inheritDoc}
	* @return <code>true</code>
	*/
	public final boolean isJSONDocument() {return true; }
	/**
	* {@inheritDoc}
	* @return <code>this</code>
	*/
	public final JSONDocument<E> asJSONDocument() {return this; }
	

}
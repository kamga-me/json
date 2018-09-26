package json;

import core.collection.IList;
import string.String;

/**
* Base class for providing support for JSON objects/nodes that are lists of nodes that are instances of {@link JSONObject JSONObject} class.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public abstract class JSONList<T extends core.Thing/*JSONObject*/> extends JSONNode implements IList<T> { //since 2018-04-22

	/**
	* Constructor.<br>
	*/
	protected JSONList() {
		super();
	}
	
	/**
	* {@inheritDoc}
	* @return {@link #JSON_ARRAY JSON_ARRAY}
	*/
	@Override
	public final byte getObjectType() {
		return JSON_ARRAY; 
	}
	/**
	* {@inheritDoc}
	* @return {@link #SJOBJECT SJOBJECT}
	*/
	@Override
	public final byte getNativeObjectType() {
		return SJOBJECT; 
	}
	/**
	* {@inheritDoc}
	*/
	@Override
	public String getName() {
		return String.EMPTY;
	}
	
	/**
	* Tells if this <code>JSONList</code> only has items that are instances of {@link JSONNode JSONNode} class.<br>
	* @return {@link #yes yes} if this <code><JSONList/code> is for sure known to only have items that are instances of {@link JSONObject JSONObject} class; {@link #maybe maybe} if this <code><JSONList/code> is for sure known to only have items that are not instances of {@link JSONObject JSONObject}, but are instances of {@link JSONNode JSONNode}; {@link #no no}, otherwise.
	*/
	public byte onlyHasNodeItems() {
		return no;
	}
	
	protected final void checkNotNullItem(final JSONNode item) {
		if (item == null) {
			throw new IllegalArgumentException(
			"JSONList::checkNotNullItem-1: null item argument"
			);
		}
	}
	
	/**
	* Adds the supplied item to the list.<br>
	* @param item the item to add - cannot be null
	* @throws IllegalArgumentException if <code>item</code> is equal to <code>null</code>
	* @throws ClassCastException if <code>item</code> is not an instance of T
	*/
	@SuppressWarnings("unchecked")
	public void addItem(final JSONNode item) {
		checkNotNullItem(item);
		add((T)item);
	}
	
	/**
	* {@inheritDoc}
	* @return <code>true</code>
	*/
	public final boolean isJSONList() {return true; }
	/**
	* {@inheritDoc}
	* @return <code>this</code>
	*/
	public final JSONList<T> asJSONList() {return this; }
	
	/**
	* Tells if this <code>JSONList</code> is an instance of {@link json.JSONArrayList JSONArrayList} class.<br>
	*/
	public boolean isJSONArrayList() {return false; }
	/**
	* @throws ClassCastException if this <code>JSONList</code> is not an instance of {@link json.JSONArrayList JSONArrayList} class.<br>
	*/
	public json.JSONArrayList<?> asJSONArrayList() {return (json.JSONArrayList)this; }
	
	/**
	* Tells if this <code>JSONList</code> is an instance of {@link json.JSONObjectArrayList JSONObjectArrayList} class.<br>
	*/
	public boolean isJSONObjectArrayList() {return false; }
	/**
	* @throws ClassCastException if this <code>JSONList</code> is not an instance of {@link json.JSONObjectArrayList JSONObjectArrayList} class.<br>
	*/
	public json.JSONObjectArrayList<?> asJSONObjectArrayList() {return (json.JSONObjectArrayList)this; }
	
	
	
}
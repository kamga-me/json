package json.stream;

//import string.ICharSequence;

import core.IValueTypes;
import core.data.type.IValueTypesExt;
import core.Value;

/**
* Interface that JSON stream readers implement.<br>
* It is worth noting that for simple properties, property key/name and property value are always fetched in two separate events.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*
* @see json.stream.IJSONSreamEventTypes IJSONSreamEventTypes
*/
public interface IJSONStreamReader<Str extends /*I*/CharSequence> extends core.data.stream.ISimpleStreamReader<Str>, IJSONSreamEventTypes {

	/**
	* {@inheritDoc}
	* @return one of the event type constants declared in interface {@link IJSONSreamEventTypes IJSONSreamEventTypes}
	*/
	public byte next();
	/**
	* {@inheritDoc}
	* @return one of the event type constants declared in interface {@link IJSONSreamEventTypes IJSONSreamEventTypes}
	*/
	public byte getEventType();
	
	/**
	* Converts the last fetched non string literal and non primitive to string.<br>
	*/
	public Str getLiteralAsString();

}
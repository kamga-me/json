package json;

import string.ICharSequence;
import string.String;
import core.IValueTypes;
import core.util.SysPropUtil;
import core.logging.Logger;

/**
* Base class for providing support for JSON objects in a easy-to-play-with and flexible manner.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public abstract class JSONObject extends JSONNode {
	
	/**
	* Empty array.<br>
	*/
	public static final JSONObject[] EMPTY_ARRAY = new JSONObject[0];

	/**
	* Constructor.<br>
	*/
	protected JSONObject() {
		super();
	}
	
	
	/**
	* {@inheritDoc}
	* @return {@link #JSON_OBJECT JSON_OBJECT}
	*/
	@Override
	public final byte getObjectType() {
		return JSON_OBJECT; 
	}
	/**
	* {@inheritDoc}
	* @return {@link #JSON JSON}
	*/
	@Override
	public final byte getNativeObjectType() {
		return JSON; 
	}
	
	/**
	* Returns the number of properties that make up this <code>JSONObject</code>.<br>
	*/
	public abstract int size();
	/**
	* Returns an iterable for iterating over the properties of this <code>JSONObject</code>.<br>
	*/
	public abstract core.IIterable<? extends JSONProperty<?>> properties();
	/**
	* Gets the value of the designated property.<br>
	* @return <code>null</code> if the JSON object does not have a property with the indicated name; {@link core.Value#NULL NULL} if the property with the indicated name has its value explicitly sets to {@link core.Value#NULL JSON_NULL}, the value of the indicated property of the JSON object, otherwise.
	*/
	public abstract core.Thing getValue(ICharSequence name, int nameStart, int nameEnd);
	/**
	* Gets the value of the designated property.<br>
	* @return <code>null</code> if the JSON object does not have a property with the indicated name; {@link core.Value#NULL NULL} if the property with the indicated name has its value explicitly sets to {@link core.Value#NULL JSON_NULL}, the value of the indicated property of the JSON object, otherwise.
	*/
	public core.Thing getValue(ICharSequence name) {return getValue(name, 0, name.length()); }
	/**
	* Gets the value of the designated property.<br>
	* @return <code>null</code> if the JSON object does not have a property with the indicated name; {@link core.Value#NULL NULL} if the property with the indicated name has its value explicitly sets to {@link core.Value#NULL JSON_NULL}, the value of the indicated property of the JSON object, otherwise.
	*/
	public abstract core.Thing getValue(CharSequence name);
	/**
	* Gets the value type of the value of the designated property.<br>
	* 
	* @see core.IValueTypes IValueTypes
	* @see core.data.type.IValueDataTypes IValueDataTypes
	*/
	public abstract byte getValueType(ICharSequence name, int nameStart, int nameEnd);
	/**
	* Gets the value type of the value of the designated property.<br>
	* 
	* @see core.IValueTypes IValueTypes
	* @see core.data.type.IValueDataTypes IValueDataTypes
	*/
	public byte getValueType(ICharSequence name) {return getValueType(name, 0, name.length()); }
	/**
	* Gets the value type of the value of the designated property.<br>
	* 
	* @see core.IValueTypes IValueTypes
	* @see core.data.type.IValueDataTypes IValueDataTypes
	*/
	public abstract byte getValueType(CharSequence name);
	
	/**
	* Tells if the properties of this <code>JSONObject</code> can be accessed by index.<br>
	* General speaking, classes implementing or having sets of properties only support access by name/key, but if any such of those classes maintains indexes for access by index, this method informs of the availability of such index.<br>  
	* @return <code>true</code> if an index for accessing the properties is maintained by/within this class; {@code false}, otherwise.
	*/
	public boolean propertiesIndexed() {
		return false; 
	}
	
	/**
	* Optional method: return the index position of the designated property.<br>
	* @return <code>0x80000000</code> if the method is not supported; the index position of the designated property, otherwise.<br>
	*
	* @see #propertiesIndexed() propertiesIndexed()
	*/
	public abstract int indexOf(final ICharSequence str, int nameStart, int nameEnd); //since 2018-04-22
	/**
	* Optional method: return the index position of the designated property.<br>
	* @return <code>0x80000000</code> if the method is not supported; the index position of the designated property, otherwise.<br>
	*
	* @see #propertiesIndexed() propertiesIndexed()
	*/
	public int indexOf(final ICharSequence name) { //since 2018-04-22
		return indexOf(name, 0, name.length());
	}
	/**
	* Optional method: gets the <code>(i + 1)<sup>th</sup></code> property.<br>
	* @param output the {name, value} structure into which the desired property must be copied.<br>
	* @throws IndexOutOfBoundsException if the method is supported and <code>i</code> is negative or 1) greater than or equal <code>this.size()</code>
	* @return <code>null</code> if the method is not supported; 
	* 
	* @see #indexOf(ICharSequence) indexOf(ICharSequence)
	* @see #indexOf(ICharSequence,int,int) indexOf(ICharSequence,int,int)
	*/
	public abstract JSONProperty<?> getProperty(int i); //since 2018-04-22
	/**
	* Optional method: gets the <code>(i + 1)<sup>th</sup></code> property.<br>
	* @param output the {name, value} structure into which the desired property must be copied.<br>
	* @throws IndexOutOfBoundsException if the method is supported and <code>i</code> is negative or 1) greater than or equal <code>this.size()</code>
	* @return <code>false</code> if the method is not supported; 
	* 
	* @see #indexOf(ICharSequence) indexOf(ICharSequence)
	* @see #indexOf(ICharSequence,int,int) indexOf(ICharSequence,int,int)
	*/
	public abstract boolean getProperty(int i, final Prop/*NVP*/ output); //since 2018-04-22
	
	/**
	* Copies the designated property into the supplied {@link Prop Prop}.<br>
	* @return <code>false</code> if no such property exists
	*/
	public abstract boolean getProperty(final ICharSequence str, final int nameStart, final int nameEnd, final Prop/*NVP*/ output); //since 2018-04-22
	
	
	protected final void checkNotNull(final core.Thing val, final byte primitiveValueType) {
		if (val == null) {
			throw new ClassCastException(
			primitiveGetMthd(primitiveValueType, "JSONObject") + "-1: cannot cast null to " + core.ValueType.get_ValueType(primitiveValueType)
			);
		}
	}
	/**
	* Returns the value of the designated property, as an <code>int</code>.<br>
	*/
	public int getInt/*Value*/(final ICharSequence name, final int nameStart, final int nameEnd) { //NOTE: removed suffix "Value" from name on 2018-04-22
		core.Thing val = getValue(name, nameStart, nameEnd);
		checkNotNull(val, IValueTypes.INT);
		return val.asNumber().intValue();
	}
	/**
	* Returns the value of the designated property, as a <code>long</code>.<br>
	*/
	public long getLong/*Value*/(ICharSequence name, final int nameStart, final int nameEnd) { //NOTE: removed suffix "Value" from name on 2018-04-22
		core.Thing val = getValue(name, nameStart, nameEnd);
		checkNotNull(val, IValueTypes.LONGINT);
		return val.asNumber().longValue();
	}
	/**
	* Returns the value of the designated property, as a <code>float</code>.<br>
	*/
	public float getFloat/*Value*/(final ICharSequence name, final int nameStart, final int nameEnd) { //NOTE: removed suffix "Value" from name on 2018-04-22
		core.Thing val = getValue(name, nameStart, nameEnd);
		checkNotNull(val, IValueTypes.SFLOAT);
		return val.asNumber().floatValue();
	}
	/**
	* Returns the value of the designated property, as a <code>double</code>.<br>
	*/
	public double getDouble/*Value*/(final ICharSequence name, final int nameStart, final int nameEnd) { //NOTE: removed suffix "Value" from name on 2018-04-22
		core.Thing val = getValue(name, nameStart, nameEnd);
		checkNotNull(val, IValueTypes.DFLOAT);
		return val.asNumber().longValue();
	}
	/**
	* Returns the value of the designated property, as a <code>boolean</code>.<br>
	*/
	public boolean getBoolean/*Value*/(final ICharSequence name, final int nameStart, final int nameEnd) { //NOTE: removed suffix "Value" from name on 2018-04-22
		core.Thing val = getValue(name, nameStart, nameEnd);
		checkNotNull(val, IValueTypes.BOOLEAN);
		return val.asBoolean().booleanValue();
	}
	
	
	/**
	* Returns the value of the designated property, as an <code>int</code>.<br>
	*/
	public int getInt/*Value*/(ICharSequence name) {return getInt/*Value*/(name, 0, name.length()); } //NOTE: removed suffix "Value" from name on 2018-04-22
	/**
	* Returns the value of the designated property, as a <code>long</code>.<br>
	*/
	public long getLong/*Value*/(ICharSequence name) {return getLong/*Value*/(name, 0, name.length()); } //NOTE: removed suffix "Value" from name on 2018-04-22
	/**
	* Returns the value of the designated property, as a <code>float</code>.<br>
	*/
	public float getFloat/*Value*/(ICharSequence name) {return getFloat/*Value*/(name, 0, name.length()); } //NOTE: removed suffix "Value" from name on 2018-04-22
	/**
	* Returns the value of the designated property, as a <code>double</code>.<br>
	*/
	public double getDouble/*Value*/(ICharSequence name) {return getDouble/*Value*/(name, 0, name.length()); } //NOTE: removed suffix "Value" from name on 2018-04-22
	/**
	* Returns the value of the designated property, as a <code>boolean</code>.<br>
	*/
	public boolean getBoolean/*Value*/(ICharSequence name) {return getBoolean/*Value*/(name, 0, name.length()); } //NOTE: removed suffix "Value" from name on 2018-04-22
	
	
	/**
	* Gets the designated property.<br>
	*/
	public abstract JSONProperty<?> getProperty(ICharSequence name, int nameStart, int nameEnd);
	/**
	* Gets the designated property.<br>
	*/
	public JSONProperty<?> getProperty(final ICharSequence name) {return getProperty(name, 0, name.length()); }
	/**
	* Gets the designated property.<br>
	*/
	public abstract JSONProperty<?> getProperty(CharSequence name);
	
	/**
	* Meant to serve to get the unnecessarily allocated resources to be released.<br>
	* If the JSON object is not trimmable, by its class, the method must do nothing.<br>
	* <b>NOTE</b>: sub-classes may need to override this method, as default implementation does nothing.<br>
	*/
	public void trim() {
		
	}
	
	/**
	* Serves to add a property to this <code>JSONObject</code>.<br>
	* @param value the value of the property - <code>null</code> must translate to {@link core.Value#NULL NULL}
	* @return <code>false</code> if a property with the same name already exists in this <code>JSONObject</code>; <code>true</code>, otherwise, of course after the property has been added.
	*/
	protected abstract boolean add_prop(final String name, final core.Thing value);
	
	/**
	* Adds a new property with an empty {@code JSONObject} value to this <code>JSONObject</code>.<br>
	* @return <code>null</code> if a property with the same name already exists in this <code>JSONObject</code>; the added property whose empty {@code JSNObject} value remains to have its properties added, otherwise.
	*/
	protected abstract JSONProperty<?> add_obj_prop(final String name);
	/**
	* Adds a new property with an empty {@code JSONList JSONList} or {@code JSONArrayBuilder} value to this <code>JSONObject</code>.<br>
	* @param jsonArrayNodeForm the form of the array node to create - valid values are those of the constants declared in class {@link JSONArrayForms JSONArrayForms}; <code>-1</code> is accepted and is interpreted as ultimate form is {@link #ARRAY_LIST ARRAY_LIST} but the items need to be accumulated first using the array builder; other values are handled as if they were {@link json.JSONArrayForms#ARRAY ARRAY}
	* @param anticipatedMinNumOfItems the anticipated minimum number of items - ignored if the array node form is {@link json.JSONForms#LINKED_LIST LINKED_LIST}
	* @param forceIntArrayIfPossible flag indicating that <code>int</code> array must be forced if possible - ignored if <code>jsonArrayNodeForm</code> is not equal to {@link json.JSONArrayForms#ARRAY ARRAY}; as a consequence, <code>byte</code> arrays are <code>short</code> arrays are forced to <code>int</code> array
	* @return <code>null</code> if a property with the same name already exists in this <code>JSONObject</code>; the added property whose empty {@code list} or array builder value remains to have its item added, otherwise.
	*
	* @see JSONArrayForms JSONArrayForms
	*/
	protected abstract JSONNode add_arr_prop(final String name, byte jsonArrayNodeForm, int anticipatedMinNumOfItems, final boolean forceIntArrayIfPossible, final boolean mustConvertToJsOnObjectList);
	
	/**
	* {@inheritDoc}
	* @return <code>true</code>
	*/
	public final boolean isJSONObject() {return true; }
	/**
	* {@inheritDoc}
	* @return <code>this</code>
	*/
	public final JSONObject asJSONObject() {return this; }
	
	/**
	* Helper Structure/class for providing support for {name, value, valueType} fully mutable triplets.<br>
	*
	* @author Marc E. KAMGA
	* @version 1.0
	* @copyright Marc E. KAMGA
	*/
	public static class Prop/*NVP*/ implements java.io.Serializable { //since 2018-04-22

		/**The class's serial version UID. */
		public static final long serialVersionUID = 0x0226EC47F1B6928AL;
		
		/**
		* The property name.<br>
		*/
		public String name;
		/**
		* The property value.<br>
		*/
		public core.Thing value;
		/**
		* The property value type.<br>
		* @see core.IValueTypes IValueTypes
		* @see core.data.type.IValueDataTypes IValueDataTypes
		*/
		public byte valueType;
		
		/**
		* Constructor.<br>
		*/
		public Prop/*NVP*/() {}
		
	}
	
	/**
	* Base class for providing support for factory objects for creating (standalone) instances of {@link JSONObject JSONObject}.<br>
	*
	* @author Marc E. KAMGA
	* @version 1.04
	* @copyright 
	*/
	public static abstract class JSONObjectFactory { //since 2018-04-23
		
		/**
		* Constructor.<br>
		*/
		JSONObjectFactory() {
			super();
		}
		/**
		* Creates a new {@link JSONObject JSONObject} whose content fully remains to be set.<br>
		*/
		public abstract JSONObject newJSONObject();
		
		
	}
	
}
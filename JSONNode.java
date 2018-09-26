package json;

import core.IValueTypes;
import core.util.SysPropUtil;
import core.logging.Logger;

/**
* Base class for providing support for JSON nodes.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public abstract class JSONNode extends dom.DOMNode implements core.ITrinaryValues, core.IUsualTinyNumbers {
	
	/**
	* Empty array.<br>
	*/
	public static final JSONNode[] EMPTY_ARRAY = new JSONNode[0];

	/**
	* Constructor.<br>
	*/
	protected JSONNode() {
		super();
	}

	/**
	* {@inheritDoc}
	* @return {@link #JSON_DOM JSON_DOM}
	*/
	public final byte getDOMType() {return JSON_DOM; }
	
	/**
	* Meant to sere to trim the node and frees all the unnecessary allocated resources.<br>
	* If the concrete class does not support trimming, the method must simply do nothing.<br>
	*/
	public void trim() { //since 2018-04-22
		//DO NOTHING, LEAINg It FOr CONCRETE CLASSES TO DO IT
	}
	/**
	* Tells if this <code>JSONNode</code> can be chained.<br>
	*/
	protected boolean canBeChained() {return false; } //since 2018-04-23
	/**
	* Returns the new <code>JSONNode</code> to this <code>JSONNode</code> in the chain, if any.<br>
	* @see #canBeChained() canBeChained()
	*/
	protected JSONNode next() {return null; } //since 2018-04-23
	/**
	* @see #canBeChained() canBeChained()
	*/
	protected void set_next(final JSONNode nxt) {
		throw new UnsupportedOperationException(
		"JSONNode::set_next-1: setting next in chain is not permitted for the class (" + this.getClass().getName() + ")"
		);
	} //since 2018-04-23
	
	/**
	* {@inheritDoc}
	* @return <code>true</code>
	*/
	@Override
	public final boolean isJSONNode() { //BUG-FIX - 2018-04-22 - was missing
		return true;
	}
	/**
	* {@inheritDoc}
	* @return <code>this</code>
	*/
	@Override
	public final JSONNode asJSONNode() { //BUG-FIX - 2018-04-22 - was missing
		return this;
	}
	
	/**
	* Tells if this <code>JSONNode</code> is an instance of {@link json.JSONList JSONList} class.<br>
	*/
	public boolean isJSONList() {return false; } //since 2018-04-22
	/**
	* @throws ClassCastException if this <code>JSONNode</code> is not an instance of {@link json.JSONList JSONList} class.<br>
	*/
	public json.JSONList<?> asJSONList() {return (json.JSONList)this; } //since 2018-04-22
	
	/**
	* Tells if this <code>JSONNode</code> is an instance of {@link json.JSONObjectList JSONObjectList} class.<br>
	*/
	public boolean isJSONObjectList() {return false; }
	/**
	* @throws ClassCastException if this <code>JSONNode</code> is not an instance of {@link json.JSONObjectList JSONObjectList} class.<br>
	*/
	public json.JSONObjectList<?> asJSONObjectList() {return (json.JSONObjectList)this; }
	
	/**
	* Tells if this <code>JSONNode</code> is an instance of {@link json.JSONProperty JSONProperty} class.<br>
	*/
	public boolean isJSONProperty() {return false; }
	/**
	* @throws ClassCastException if this <code>JSONNode</code> is not an instance of {@link json.JSONProperty JSONProperty} class.<br>
	*/
	public json.JSONProperty<?> asJSONProperty() {return (json.JSONProperty)this; }
	
	/**
	* Tells if this <code>JSONNode</code> is an instance of {@link json.JSONDocument JSONDocument} class.<br>
	*/
	public boolean isJSONDocument() {return false; }
	/**
	* @throws ClassCastException if this <code>JSONNode</code> is not an instance of {@link json.JSONDocument JSONDocument} class.<br>
	*/
	public json.JSONDocument<?> asJSONDocument() {return (json.JSONDocument)this; }
	
	/**
	* Package private method: tells if this <code>JSONNode</code> is an instance of {@link json.JSONDocument JSONDocument} class.<br>
	*/
	boolean isJSONArrayBuilder() {return false; }
	/**
	* @throws ClassCastException if this <code>JSONNode</code> is not an instance of {@link json.JSONDocument JSONDocument} class.<br>
	*/
	JSONArrayBuilder asJSONArrayBuilder() {return (JSONArrayBuilder)this; }
	
	/**
	* Package private method: tells if this <code>JSONNode</code> is an instance of {@link json.JSONChainableThingNode JSONChainableThingNode} class.<br>
	*/
	boolean isJSONChainableThingNode() {return false; }
	/**
	* @throws ClassCastException if this <code>JSONNode</code> is not an instance of {@link json.JSONChainableThingNode JSONChainableThingNode} class.<br>
	*/
	JSONChainableThingNode asJSONChainableThingNode() {return (JSONChainableThingNode)this; }
	
	
	protected static final CharSequence primitiveGetMthd(final byte primitiveValueType, final CharSequence ofClass) {
		switch(primitiveValueType)
		{
		case IValueTypes.INT:
			return ofClass + "::getInt"; 
		case IValueTypes.LONGINT:
			return ofClass + "::getLong"; 
		case IValueTypes.SFLOAT:
			return ofClass + "::getFloat"; 
		case IValueTypes.DFLOAT:
			return ofClass + "::getDouble"; 
		case IValueTypes.BOOLEAN:
			return ofClass + "::getBoolean"; 
		case IValueTypes.TINYINT:
			return ofClass + "::getByte"; 
		case IValueTypes.SHORTINT:
			return ofClass + "::getShort"; 
		default: 
			return ofClass + "::getChar"; 
		}
	}
			
	
	protected static class ChainedNodeListIterator/* implements core.IIterable<JSONNode>*/ {
		
		protected JSONNode next;
		protected JSONNode current;
		
		protected ChainedNodeListIterator(final JSONNode first) {
			this.next = first;
			this.current = null;
		}
		/**
		* {@inheritDoc}
		*/
		public boolean next() {
			if (next != null) {
				current = next;
				next = next.next();
				return true;
			}
			return false;
		}
		/**
		* {@inheritDoc}
		*/
		public JSONNode current() {return current; }
		
	}
	/**
	* TODO: effectively code this method.<br>
	*/
	protected static Logger getLogger() {
		return null;
	}
	
	/**
	* The number of items from which it is worth doing/starting binary search.<br>
	*/
	protected static final byte BINARY_SEARCH_START; // = 65; //since 2018-04-25
	
	protected static final byte BUFFERED_AUTO_FLUSH_SIZE; // = 16; //since 2018-04-25
	
	protected static final short OBJ_PROP_VALUE_EMPTY_PROP_LST_INITIAL_CAPACITY = SysPropUtil.getNonNegativeShortIntSysProp("json.set.objectPropertyValue.emptyPropertyList.initialCapacity", ZERO/*default*/, getLogger());
	static {
		Logger LOGGER = getLogger();
		short val = SysPropUtil.getNonNegativeShortIntSysProp("json.set.BINARY_SEARCH_START", (short)65, LOGGER);
		if (val < (byte)9 || val > (byte)65) {
			if (LOGGER != null) {
				if (val > (short)8) {
					LOGGER.error("invalid value for property json.set.BINARY_SEARCH_START, changed it to 65", "value", java.lang.String.valueOf(val));
					val = 65;
				}
				else {
					LOGGER.warning("value for property json.set.BINARY_SEARCH_START too small, changed it to 17", "value", java.lang.String.valueOf(val));
					val = 17;
				}
			}
		}
		BINARY_SEARCH_START = (byte)val;
		val = SysPropUtil.getNonNegativeShortIntSysProp("json.set.BUFFERED_AUTO_FLUSH_SIZE", (short)65, LOGGER);
		if (val < (byte)4 || val > BINARY_SEARCH_START) {
			LOGGER.warning("invalid value or value too small for property json.set.BINARY_SEARCH_START, changed it to " + (val > BINARY_SEARCH_START ? BINARY_SEARCH_START : 4), "value", java.lang.String.valueOf(val));
			val = (val > BINARY_SEARCH_START ? BINARY_SEARCH_START : (byte)4);
		}
		BUFFERED_AUTO_FLUSH_SIZE = (byte)val;
	}
	
}
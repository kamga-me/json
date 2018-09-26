package json;

import core.IValueTypes;

/**
* Package private class for providing support for JSON nodes.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
class JSONChainableThingNode extends JSONNode {

	/**The class's serial version UID. */
	public static final long serialVersionUID = 0x0227026601B6928AL;
	
	protected core.Thing thing;
	protected JSONNode nextInChain;

	/**
	* Constructor.<br>
	*/
	protected JSONChainableThingNode(core.Thing thing) {
		super();
		this.thing = thing;
	}
	
	protected final boolean canBeChained() {return true; } //since 2018-04-23
	
	protected final void set_next(final JSONNode nxt) {
		nextInChain = nxt;
	}
	protected final JSONNode next() {return nextInChain; } //since 2018-04-23

	final boolean isJSONChainableThingNode() {return true; }
	
	final JSONChainableThingNode asJSONChainableThingNode() {
		return this;
	}
	
}
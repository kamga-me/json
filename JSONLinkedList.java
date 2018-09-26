package json;

import core.collection.IList;

import static core.collection.util.ArrayCapacityIncrUtil.incrCapacityByHalf;

/**
* Class for providing support for JSON nodes that are actually node lists that chain/link their node items.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public class JSONLinkedList extends JSONList<core.Thing> {

	/**The class's serial version UID. */
	public static final long serialVersionUID = 0x0227036471B6928AL;
	
	protected JSONNode first;
	protected JSONNode last;
	protected int size;
	byte onlyHasNodeItems;
	
	/**
	* Constructor.<br>
	*/
	JSONLinkedList() {
		size = 0;
		onlyHasNodeItems = no;
	}
	
	/**
	* {@inheritDoc}
	*/
	public byte onlyHasNodeItems() {
		return onlyHasNodeItems;
	}
	
	/**
	* {@inheritDoc}
	*/
	public boolean isEmpty() {return size == 0; }
	/**
	* {@inheritDoc}
	*/
	public int size() {return size; }
	
	final core.Thing item(final JSONNode node) {
		return size == 0 ? null : node.isJSONChainableThingNode() ? node.asJSONChainableThingNode().thing : node;
	}
	/**
	* {@inheritDoc}
	*/
	public core.Thing first() {
		return item(first);
	}
	/**
	* {@inheritDoc}
	*/
	public core.Thing last() {
		return item(last);
	}
	
	/**
	* Adds the supplied item to the list.<br>
	* @param item the item to add - may be null; null translates to {@link core.Value#NULL NULL}
	*/
	public void add(core.Thing item) {
		if (item == null) {
			item = core.Value.NULL;
		}
		if (item.isDOMNode()) {
			add__(item.asDOMNode().asJSONNode());
		}
		if (onlyHasNodeItems != no) {
			onlyHasNodeItems = no;
		}
		if (size == 0) {
			__addFirst(new JSONChainableThingNode(item));
			return ;
		}
		add__(new JSONChainableThingNode(item));
	}
	protected final void checkNotNullChainableItem(final JSONNode item) {
		checkNotNullItem(item);
		if (!item.canBeChained()) {
			throw new IllegalArgumentException(
			"JSONLinkedList::checkNotNullChainableItem-1: not a chainable item"
			);
		}
	}
	/**
	* Adds the supplied item to the list.<br>
	* @param item the item to add - cannot be null
	* @throws IllegalArgumentException if <code>item</code> is equal to <code>null</code>
	*/
	public void add(final JSONNode item) {
		checkNotNullChainableItem(item);
		if (size == 0) {
			if (item.isJSONObject()) {
				onlyHasNodeItems = yes;
			}
			else {
				onlyHasNodeItems = maybe;
			}
			__addFirst(item);
			return ;
		}
		else if (onlyHasNodeItems == yes && !item.isJSONObject()) {
			onlyHasNodeItems = maybe;
		}
		add__(item);
	}
	/**
	*{@inheritDoc}
	*/
	@Override
	public final void addItem(final JSONNode item) {
		add(item);
	}
	final void __addFirst(final JSONNode item) {
		first = item;
		last = first;
		size = 1;
	}
	
	final void add__(final JSONNode item) {
		last.set_next(item);
		last = item;
		size++;
	}
	/**
	* [@inheritDoc]
	*/
	public core.Thing remove(core.Thing item) {
		if (size == 0) return null;
		if (item == null) {
			item = core.Value.NULL;
		}
		JSONNode prev = null;
		for (JSONNode n=first;n!=null;n=n.next())
		{
			if (n.isJSONChainableThingNode()) {
				JSONChainableThingNode node = n.asJSONChainableThingNode();
				if (node.thing == item || node.thing.equals(item)) {
					removeNode(n, prev);
				}
				return node.thing;
			}
			else if (n == item || n.equals(item)) {
				removeNode(n, prev);
				return n;
			}
			prev = n;
		}
		return null;
	}
	private final void removeNode(final JSONNode n, final JSONNode prev) {
		if (n == first) {
			first = n.next();
			size--;
			if (size == 0) {
				last = first;
			}
		}
		else {
			prev.set_next(n.next());
			size--;
		}
	}
	
	/**
	* {@inheritDoc}
	*/
	public final void getElements(final core.Thing[] dest, int destOff) {
		if (size == 0) return ;
		JSONNode node = first;
		int i = 1;
		do 
		{
			if (node.isJSONChainableThingNode()) {
				dest[destOff++] = node.asJSONChainableThingNode().thing;
			}
			else{
				dest[destOff++] = node;
			}
			i++;
			if (i < size) {
				node = node.next();
				if (node == null) {
					throw new IllegalStateException(
					"JSONLinkedList::getElements-1: unexpected null next node (index=" + i + ")"
					);
				}
				continue ;
			}
			return ;
		} while (true);
	}
	/**
	* {@inheritDoc}
	*/
	public core.Thing[] toArray() {
		if (size == 0) return onlyHasNodeItems == yes ? JSONObject.EMPTY_ARRAY : onlyHasNodeItems == maybe ? JSONNode.EMPTY_ARRAY : core.Thing.EMPTY_ARRAY;
		core.Thing[] arr = onlyHasNodeItems == yes ? new JSONObject[size] : onlyHasNodeItems == maybe ? new JSONNode[size] : new core.Thing[0];
		getElements(arr, 0);
		return arr;
	}
	
	public core.IIterable<core.Thing> iterator() {
		return new Iter(first);
	}
	
	protected class Iter extends ChainedNodeListIterator implements core.IIterable<core.Thing> {
		
		protected Iter(final JSONNode first) {
			super(first);
		}
		
		public int size() {return size; }
		
		public core.Thing get() {
			return item(current);
		}
		
		public core.Thing Get() {return get(); }
		
	}
	
}
package json;

import core.collection.IList;

import static core.collection.util.ArrayCapacityIncrUtil.incrCapacityByHalf;

/**
* 
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public class JSONArrayList<T extends core.Thing> extends JSONList<T> {

	/**The class's serial version UID. */
	public static final long serialVersionUID = 0x0227018481B6928AL;
	
	protected T[] items;
	protected int size;
	protected byte allJsonNodeItems;
	
	/**
	* Constructor.<br>
	*/
	public JSONArrayList(int initialCapacity) {
		this.items = initialCapacity == 0 ? __getEmptyArray() : __newArray(initialCapacity);
		this.size = 0;
		this.allJsonNodeItems = maybe;
	}
	/**
	* Constructor.<br>
	*/
	public JSONArrayList() {
		this(0);
	}
	JSONArrayList(T[] items, final byte allJsonNodeItems) {
		this.items = items;
		this.size = items.length;
		this.allJsonNodeItems = allJsonNodeItems;
	}
	
	/**
	* {@inheritDoc}
	*/
	@Override
	public byte onlyHasNodeItems() {
		return allJsonNodeItems == yes ? maybe : no;
	}
	
	/**
	* {@inheritDoc}
	*/
	public boolean isEmpty() {return size == 0; }
	/**
	* {@inheritDoc}
	*/
	public int size() {return size; }
	
	/**
	* GEts the <code>(i + 1)<sup>th</sup></code> item.<br>
	*/
	public T get(int i) {
		return items[i]; 
	}
	
	/**
	* {@inheritDoc}
	*/
	public final T first() {
		return items[0];
	}
	/**
	* {@inheritDoc}
	*/
	public final T last() {
		return items[size - 1];
	}
	
	protected final void ensureCanAddOneMoreItem() {
		if (size == 0) {
			if (items == null || items.length == 0) {
				items = __newArray(10);
			}
			return ;
		}
		else if (size >= items.length) {
			int newCapacity = items.length > 2014 ? items.length + (items.length >>> 2) : items.length + (items.length >>> 1);
			T[] arr = __newArray(newCapacity <= size ? size + 2 : newCapacity);
			System.arraycopy(items, 0, arr, 0, size);
			items = arr;
		}
	}
	
	public void add(final T item) {
		if (item == null) {
			throw new JSONException(
			"JSONArrayList::add-1: null item"
			);
		}
		ensureCanAddOneMoreItem();
		items[size++] = item;
		
	}
	/**
	* Returns the index of the (first) item that is equal to that supplied.<br>
	*/
	public int indexOf(final T item) {
		if (item == null) return -1;
		for (int i=0;i<size;i++)
		{
			T itm = items[i];
			if (itm == item || itm.equals(item)) return i;
		}
		return -1;
	}
	
	public T remove(final T item)  {
		int index = indexOf(item);
		if (index < 0) return null;
		T removed = items[index];
		size--;
		if (index < size) { //here size is equal to the new size (size after remove)
			System.arraycopy(items, index + 1, items, index, size - index);
		}
		return removed;
	}
	
	/**
	* {@inheritDoc}
	*/
	public void getElements(final T[] dest, int destOff) {
		System.arraycopy(items, 0, dest, destOff, size);
	}
	/**
	* {@inheritDoc}
	*/
	public T[] toArray() {
		T[] arr = size == 0 ? __getEmptyArray() : __newArray(size);
		if (size != 0) {
			System.arraycopy(items, 0, arr, 0, size);
		}
		return arr;
	}
	/**
	* {@inheritDoc}
	*/
	public core.IIterable<T> iterator() {return new Iter(); }
	
	/**
	* {@inheritDoc}
	* @return <code>true</code>
	*/
	@Override
	public final boolean isJSONArrayList() {
		return true; 
	}
	/**
	* {@inheritDoc}
	* @return <code>this</code>
	*/
	@Override
	public final JSONArrayList<T> asJSONArrayList() {return this; }
	
	@SuppressWarnings("unchecked")
	protected T[] __getEmptyArray() {
		return (T[])core.Thing.EMPTY_ARRAY;
	}
	@SuppressWarnings("unchecked")
	protected T[] __newArray(int len) {
		return (T[])new core.Thing[len];
	}
	/**
	* {@inheritDoc}
	*/
	@Override
	public void trim(){
		if (size == 0) {
			items = __getEmptyArray();
			return ;
		}
		else if (size < items.length) {
			T[] arr = __newArray(size);
			System.arraycopy(items, 0, arr, 0, size);
			items = arr;
		}
	}
	
	protected class Iter implements core.IIterable<T> {
		
		protected int currentIndex;
		protected int lastIndex;
		
		public Iter() {
			currentIndex = -1;
			lastIndex = size - 1;
		}
		
		public int size() {
			return JSONArrayList.this.size;
		}
		
		public boolean next() {
			if (currentIndex < lastIndex) {
				currentIndex++;
				return true; 
			}
			return false;
		}
		
		public T get() {return items[currentIndex]; }
		
		public T Get() {return get(); }
		
		
	}


}
package json;

import string.ICharSequence;
import string.String;
import core.IValueTypes;
import core.ValueArrayBuilder;
import core.Array;
import core.Array.ValueArray;
import static number.util.IntegerNumbersCache.getIntegerNumber;
import number.DFloat;
import number.SFloat;

import static core.collection.util.ArrayCapacityIncrUtil.incrCapacityByHalf;

import core.AnyItemArray;
import static core.AnyItemArray.new_AnyItemArray;

/**
* This is more a helper node class for building array nodes.<br>
* Once the build is complete method {@link #toArray() toArray()} is used to get the finalized array value.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
/*public */class JSONArrayBuilder extends JSONNode implements core.ITrinaryValues {

	/**The class's serial version UID. */
	public static final long serialVersionUID = 0x0226F84E41B6928AL;
	
	ValueArrayBuilder valueArrayBuilder;
	core.Thing[] anyItemArray;
	protected int anyItemsCount;
	protected int anticipatedMinNumOfItems;
	protected int capacityIncrByQtrThreshold;
	protected byte isJSONObjectArray;
	protected boolean allJsonNodeItems;
	
	protected boolean forceIntArrayIfPossible;
	protected boolean arrayListFormPreferred;
	protected boolean mustConvertToJsOnObjectList;
	
	/**
	* Constructor.<br>
	*/
	JSONArrayBuilder(final int anticipatedMinNumOfItems, final boolean forceIntArrayIfPossible, final boolean arrayListFormPreferred, boolean mustConvertToJsOnObjectList) {
		super();
		this.anyItemsCount = 0;
		this.anyItemArray = core.Thing.EMPTY_ARRAY;
		this.anticipatedMinNumOfItems = anticipatedMinNumOfItems < 0 ? 10 : anticipatedMinNumOfItems;
		this.capacityIncrByQtrThreshold = 1024;
		this.isJSONObjectArray = maybe;
		this.allJsonNodeItems = true; //set it to true for empty array
		this.forceIntArrayIfPossible = forceIntArrayIfPossible;
		this.arrayListFormPreferred = arrayListFormPreferred;
		this.mustConvertToJsOnObjectList = mustConvertToJsOnObjectList;
	}
	/**
	* Constructor.<br>
	*/
	JSONArrayBuilder(final int anticipatedMinNumOfItems, final boolean forceIntArrayIfPossible, final boolean arrayListFormPreferred) {
		this(anticipatedMinNumOfItems, forceIntArrayIfPossible, arrayListFormPreferred, false/*mustConvertToJsOnObjectList*/);
	}
	/**
	* Constructor.<br>
	*/
	JSONArrayBuilder(final int anticipatedMinNumOfItems, final boolean forceIntArrayIfPossible) {
		this(anticipatedMinNumOfItems, forceIntArrayIfPossible, false/*arrayListFormPreferred*/);
	}
	/**
	* Constructor.<br>
	*/
	JSONArrayBuilder(final int anticipatedMinNumOfItems) {
		this(anticipatedMinNumOfItems, false/*forceIntArrayIfPossible*/);
	}
	
	private final void __add(final core.Thing value, final boolean stillArrayOfJSONObjects) {
		if (allJsonNodeItems && (!stillArrayOfJSONObjects || !value.isDOMNode())) {
			allJsonNodeItems = false;
		}
		if (anyItemsCount == 0) {
			if (anyItemArray == null || anyItemArray.length < anticipatedMinNumOfItems) {
				anyItemArray = stillArrayOfJSONObjects ? new JSONObject[anticipatedMinNumOfItems] : new core.Thing[anticipatedMinNumOfItems];
			}
			anyItemArray[0] = value;
			anyItemsCount =  1;
			if (!stillArrayOfJSONObjects) {
				isJSONObjectArray = stillArrayOfJSONObjects ? yes : no;
			}
			return ;
		}
		if (anyItemsCount >= anyItemArray.length) {
			int newCapacity = anyItemArray.length + (anyItemArray.length > capacityIncrByQtrThreshold ? (anyItemArray.length >>> 2) : (anyItemArray.length >>> 1));
			core.Thing[] arr;
			if (stillArrayOfJSONObjects) {
				arr = new JSONObject[newCapacity <= anyItemsCount ? anyItemsCount + 2 : newCapacity];
			}
			else {
				arr = new core.Thing[newCapacity <= anyItemsCount ? anyItemsCount + 2 : newCapacity];
				if (isJSONObjectArray != no) {
					isJSONObjectArray = no;
				}
			}
			System.arraycopy(anyItemArray, 0, arr, 0, anyItemsCount);
			anyItemArray = arr;
		}
		else if (!stillArrayOfJSONObjects && isJSONObjectArray != no) {
			isJSONObjectArray = no;
		}
		anyItemArray[anyItemsCount++] = value;
		
	}

	public void addItem(final int value) {
		if (anyItemsCount == 0) {
			if (valueArrayBuilder == null) {
				valueArrayBuilder = new ValueArrayBuilder(value, anticipatedMinNumOfItems);
				return ;
			}
			else if (valueArrayBuilder.isEmpty()) {
				valueArrayBuilder.reset(value, anticipatedMinNumOfItems);
			}
			else {
				valueArrayBuilder.add(value);
			}
			return ;
		}
		__add(getIntegerNumber(value), false/*stillArrayOfJSONObjects*/);
	}
	public void addItem(final long value) {
		if (anyItemsCount == 0) {
			if (valueArrayBuilder == null) {
				valueArrayBuilder = new ValueArrayBuilder(value, anticipatedMinNumOfItems);
				return ;
			}
			else if (valueArrayBuilder.isEmpty()) {
				valueArrayBuilder.reset(value, anticipatedMinNumOfItems);
			}
			else {
				valueArrayBuilder.add(value);
			}
			return ;
		}
		__add(value >= 0x80000000 && value <= 0x7FFFFFFF ? getIntegerNumber((int)value) : new number.LongInt(value), false/*stillArrayOfJSONObjects*/);
	}
	public void addItem(final float value) {
		if (anyItemsCount == 0) {
			if (valueArrayBuilder == null) {
				valueArrayBuilder = new ValueArrayBuilder(value, anticipatedMinNumOfItems);
				return ;
			}
			else if (valueArrayBuilder.isEmpty()) {
				valueArrayBuilder.reset(value, anticipatedMinNumOfItems);
			}
			else {
				valueArrayBuilder.add(value);
			}
			return ;
		}
		__add(SFloat.valueOf(value), false/*stillArrayOfJSONObjects*/);
	}
	public void addItem(final double value) {
		if (anyItemsCount == 0) {
			if (valueArrayBuilder == null) {
				valueArrayBuilder = new ValueArrayBuilder(value, anticipatedMinNumOfItems);
				return ;
			}
			else if (valueArrayBuilder.isEmpty()) {
				valueArrayBuilder.reset(value, anticipatedMinNumOfItems);
			}
			else {
				valueArrayBuilder.add(value);
			}
			return ;
		}
		__add(DFloat.valueOf(value), false/*stillArrayOfJSONObjects*/);
	}
	public void addItem(final boolean value) {
		if (anyItemsCount == 0) {
			if (valueArrayBuilder == null) {
				valueArrayBuilder = new ValueArrayBuilder(value, anticipatedMinNumOfItems);
				return ;
			}
			else if (valueArrayBuilder.isEmpty()) {
				valueArrayBuilder.reset(value, anticipatedMinNumOfItems);
			}
			else {
				valueArrayBuilder.add(value);
			}
			return ;
		}
		__add(core.Boolean.valueOf(value), false/*stillArrayOfJSONObjects*/);
	}
	/**
	* Accumulates the supplied item: adds a the supplied item to internal array/container of this JSON array builder.<br>
	*/
	public void addItem(/*final */core.Value value) {
		if (mustConvertToJsOnObjectList) {
			throw new JSONException(
			"JSONArrayBuilder::addItem-1: non JSONObject item"
			);
		}
		if (value == null) {
			value = core.Value.NULL;
		}
		if (anyItemsCount == 0) {
			if (valueArrayBuilder == null) {
				valueArrayBuilder = new ValueArrayBuilder(value, anticipatedMinNumOfItems);
				return ;
			}
			else if (valueArrayBuilder.isEmpty()) {
				valueArrayBuilder.reset(value, anticipatedMinNumOfItems);
			}
			else {
				valueArrayBuilder.add(value);
			}
			return ;
		}
		__add(value, false/*stillArrayOfJSONObjects*/);
	}
	/**
	* Accumulates the supplied item: adds it to internal array/container of this JSON array builder.<br>
	*/
	public void addItem(/*final */core.Thing item) {
		if (item == null || item.isValue()) {
			addItem(item == null ? core.Value.NULL : item.asValue());
			return ;
		}
		else if (item.isDOMNode()) {
			dom.DOMNode domNode = item.asDOMNode();
			if (domNode.isJSONObject()) {
				addItem(item.asDOMNode().asJSONObject());
				return ;
			}
			else if (mustConvertToJsOnObjectList) {
				throw new JSONException(
				"JSONArrayBuilder::addItem-2: non JSONObject item"
				);
			}
			else if (allJsonNodeItems && !domNode.isJSONNode()) {
				allJsonNodeItems = false;
				if (isJSONObjectArray != no) {
					isJSONObjectArray = no;
				}
			}
		}
		else if (allJsonNodeItems) {
			allJsonNodeItems = false;
			if (isJSONObjectArray != no) {
				isJSONObjectArray = no;
			}
		}
		if (anyItemsCount == 0) {
			switch_to_any_item_array();
		}
		__add(item, false/*stillArrayOfJSONObjects*/);
	}
	
	private final void switch_to_any_item_array() {
		if (anyItemArray == null || anticipatedMinNumOfItems >= valueArrayBuilder.length()) {
			anyItemArray = new core.Thing[anticipatedMinNumOfItems < valueArrayBuilder.length() ? valueArrayBuilder.length() + 4 : anticipatedMinNumOfItems + 4];
		}
		anyItemArray = valueArrayBuilder.copy(anyItemArray);
		anyItemsCount = valueArrayBuilder.length();
		valueArrayBuilder.reset();
	}
	
	public void addItem(final JSONObject item) {
		if (anyItemsCount == 0) {
			if (valueArrayBuilder != null && !valueArrayBuilder.isEmpty()) {
				switch_to_any_item_array();
				__add(item, false/*stillArrayOfJSONObjects*/);
				return ;
			}
			isJSONObjectArray = yes;
			__add(item, true/*stillArrayOfJSONObjects*/);
			return ;
		}
		__add(item, isJSONObjectArray == yes/*stillArrayOfJSONObjects*/);
	}
	
	static final AnyItemArray<?> EMPTY = new_AnyItemArray(core.Thing.EMPTY_ARRAY);
	
	/**
	* Converts the accumulated/buffered items to an {@link core.Array Array}.<br>
	*/
	public final Array<?> toArray() {
		if (anyItemsCount == 0) {
			return (valueArrayBuilder == null || valueArrayBuilder.isEmpty()) ? EMPTY : valueArrayBuilder.toValueArray(forceIntArrayIfPossible);
		}
		else if (isJSONObjectArray == yes) {
			JSONObject[] arr = new JSONObject[anyItemsCount];
			System.arraycopy(anyItemArray, 0, arr, 0, anyItemsCount);
			AnyItemArray<JSONObject> arr_ = new_AnyItemArray(arr);
			return arr_;
		}
		else {
			core.Thing[] arr = new core.Thing[anyItemsCount];
			System.arraycopy(anyItemArray, 0, arr, 0, anyItemsCount);
			return new_AnyItemArray(arr);
		}
	}
	/**
	* Converts the (current) content of the builder to a {@link JSONObjectArrayList JSONObjectArrayList}.<br>
	* @throws JSONException if there is at least one item that is not a {@link JSONObject JSONObject}
	*/
	public JSONObjectArrayList<?> toJSONObjectList() {
		if (anyItemsCount == 0 || valueArrayBuilder == null || valueArrayBuilder.isEmpty()) {
			return new JSONObjectArrayList<JSONObject>(JSONObject.EMPTY_ARRAY);
		}
		else if (isJSONObjectArray == yes) {
			JSONObject[] items = new JSONObject[anyItemsCount];
			System.arraycopy(anyItemArray, 0, items, 0, anyItemsCount);
			return new JSONObjectArrayList<JSONObject>(items);
		}
		throw new JSONException(
		"JSONArrayBuilder::toObjectList-1: cannot convert non JSONObject items to JSONObject"
		);
	}
	/**
	* Converts the (current) content of the builder to a {@link JSONArrayList JSONArrayList}.<br>
	*/
	public JSONArrayList<?> toJSONList() {
		if (anyItemsCount == 0 || valueArrayBuilder == null || valueArrayBuilder.isEmpty()) {
			return new JSONArrayList<core.Thing>(core.Thing.EMPTY_ARRAY, maybe/*allJsonNodeItems*/);
		}
		else {
			core.Thing[] items = new core.Thing[anyItemsCount];
			System.arraycopy(anyItemArray, 0, items, 0, anyItemsCount);
			return new JSONArrayList<core.Thing>(items, allJsonNodeItems ? yes : no);
		}
	}
	/**
	* Reset the array builder.<br>
	*/
	public void reset() {
		anyItemsCount = 0;
		if (valueArrayBuilder != null && !valueArrayBuilder.isEmpty()) {
			valueArrayBuilder.reset();
		}
		isJSONObjectArray = maybe;
		allJsonNodeItems = true; //set it to true for empty array
	}
	
	/**
	* Converts the content of this <code>JSONArrayBuilder</code> to a JSON array node.<br>
	* @return an {@link core.Array Array} or a {@link JSONList JSONList}
	*/
	public final core.Thing toArrayNode() { //since 2018-04-23
		return arrayListFormPreferred ? mustConvertToJsOnObjectList ? toJSONObjectList() : toJSONList() : toArray();
	}
	
	/**
	* {@inheritDoc}
	* @return <code>this</code>
	*/
	@Override
	final boolean isJSONArrayBuilder() {return true; }
	/**
	* {@inheritDoc}
	* @return <code>this</code>
	*/
	@Override
	final JSONArrayBuilder asJSONArrayBuilder() {return this; }

}
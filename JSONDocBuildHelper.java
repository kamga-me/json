package json;

import string.ICharSequence;
import string.String;

import core.document.SimpleDocBuildHelperAdapter;

import static number.util.IntegerNumbersCache.getIntegerNumber;
import number.LongInt;
import number.DFloat;
import number.SFloat;

import core.Value;

import json.JSONObject.JSONObjectFactory;

/**
* Almost concrete class for providing support for simple document build helpers for JSON.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public abstract class JSONDocBuildHelper<T extends core.Thing> extends SimpleDocBuildHelperAdapter<JSONNode/*N*/,JSONDocument<core.Thing>,JSONNode/*S*/,T> {

	/**
	* The form of the JSON array nodes to create - valid values are those of the constants declared in class {@link JSONArrayForms JSONArrayForms}; <code>-1</code> is accepted and is interpreted as ultimate form is {@link #ARRAY_LIST ARRAY_LIST} but the items need to be accumulated first using the array builder; other values are handled as if they were {@link json.JSONArrayForms#ARRAY ARRAY}.<br>
	*
	* @see json.JSONArrayForms JSONArrayForms
	*/
	/*protected */byte jsonArrayNodeForm;
	protected boolean forceIntArrayIfPossible;
	protected boolean arrayListFormPreferred;
	protected byte mustConvertToJsOnObjectList;
	
	protected JSONObjectFactory standaloneJsonObjFactory;

	/**
	* Constructor.<br>
	* @param jsonArrayNodeForm the form of the JSON array nodes to create - valid values are those of the constants declared in class {@link JSONArrayForms JSONArrayForms}; <code>-1</code> is accepted and is interpreted as ultimate form is {@link #ARRAY_LIST ARRAY_LIST} but the items need to be accumulated first using the array builder; other values are handled as if they were {@link json.JSONArrayForms#ARRAY ARRAY}.<br>
	*/
	/*protected */JSONDocBuildHelper(byte jsonArrayNodeForm, final boolean forceIntArrayIfPossible, final boolean arrayListFormPreferred, final JSONObjectFactory standaloneJsonObjFactory) {
		super();
		this.jsonArrayNodeForm = jsonArrayNodeForm;
		this.forceIntArrayIfPossible = forceIntArrayIfPossible;
		this.arrayListFormPreferred = arrayListFormPreferred;
		this.mustConvertToJsOnObjectList = maybe;
		this.standaloneJsonObjFactory = standaloneJsonObjFactory;
	}
	/**
	* Constructor.<br>
	* @param jsonArrayNodeForm the form of the JSON array nodes to create - valid values are those of the constants declared in class {@link JSONArrayForms JSONArrayForms}; <code>-1</code> is accepted and is interpreted as ultimate form is {@link #ARRAY_LIST ARRAY_LIST} but the items need to be accumulated first using the array builder; other values are handled as if they were {@link json.JSONArrayForms#ARRAY ARRAY}.<br>
	*/
	/*protected */JSONDocBuildHelper(byte jsonArrayNodeForm, final boolean forceIntArrayIfPossible, final boolean arrayListFormPreferred) {
		this(jsonArrayNodeForm, forceIntArrayIfPossible, arrayListFormPreferred, null/*standaloneJsonObjFactory*/);
	}
	/**
	* Constructor.<br>
	* @param jsonArrayNodeForm the form of the JSON array nodes to create - valid values are those of the constants declared in class {@link JSONArrayForms JSONArrayForms}; <code>-1</code> is accepted and is interpreted as ultimate form is {@link #ARRAY_LIST ARRAY_LIST} but the items need to be accumulated first using the array builder; other values are handled as if they were {@link json.JSONArrayForms#ARRAY ARRAY}.<br>
	*/
	/*protected */JSONDocBuildHelper(byte jsonArrayNodeForm) {
		this(jsonArrayNodeForm, false/*forceIntArrayIfPossible*/, false/*arrayListFormPreferred*/);
	}
	/**
	* Constructor.<br>
	* The form of arrays to build is {@link json.JSONArrayForms#ARRAY ARRAY}.<br>
	*/
	/*protected */JSONDocBuildHelper() {
		this(JSONArrayForms.ARRAY);
	}
	
	public void setMustConvertToJsonObjectList(final byte value) {
		this.mustConvertToJsOnObjectList = value < ZERO ? no : value == ZERO ? maybe : yes;
	}
	public void setMustConvertToJsonObjectList(final boolean value) {
		this.mustConvertToJsOnObjectList = value ? yes : no;
	}
	/**
	* Does same as <code>setMustConvertToJsonObjectList(true)</code>.<br>
	* @return <code>this</code>
	*/
	public JSONDocBuildHelper<T> setMustConvertToJsonObjectList() {
		setMustConvertToJsonObjectList(true); return this; 
	}

	protected final JSONException new_xcptn(java.lang.String errMsg) {
		throw new JSONException(errMsg);
	}

	protected final void err(final int errNum, final CharSequence errMsg, final int mthdNum) {
		err(errNum, errMsg, mthd(mthdNum));
	}
	
	/**
	* Creates a new standalone {@link JSONObject JSONObject}.<br>
	*/
	protected final JSONObject newJSONObject() {
		return standaloneJsonObjFactory != null ? standaloneJsonObjFactory.newJSONObject() : new JSONObjectImpl1();
	}

	public JSONDocument<core.Thing> startDoc() {
		return new JSONDocument<core.Thing>();
	}

	/**
	* {@inheritDoc}
	*/
	public boolean add/*set*/(final String name, final core.Thing value, final JSONNode to) {
		if (to.isJSONObject()) {
			return to.asJSONObject().add_prop(name, value == null ? core.Value.NULL : value);
		}
		else if (to.isJSONProperty()) {
			return to.asJSONProperty().add_prop_to_obj_val(name, value == null ? core.Value.NULL : value);
		}
		err(1/*errNum*/, "cannot add a property to a non JSONObject node", 1/*mthdNum*/); return false; //dummy return
	}
	/**
	* {@inheritDoc}
	*/
	public boolean add/*set*/(final String name, final int value, final JSONNode to) {
		return add(name, getIntegerNumber(value), to);
	}
	/**
	* {@inheritDoc}
	*/
	public boolean add/*set*/(String name, final long value, final JSONNode to) {
		return add(name, __getIntegerNumber(value), to);
	}
	protected static final number.IntegerNumber __getIntegerNumber(final long value) {
		return value < 0x80000000 || value > 0x7FFFFFFF ? new LongInt(value) : getIntegerNumber((int)value);
	}
	/**
	* {@inheritDoc}
	*/
	public boolean add/*set*/(final String name, final float value, final JSONNode to) {
		return add(name, SFloat.valueOf(value), to);
	}
	/**
	* {@inheritDoc}
	*/
	public boolean add/*set*/(final String name, final double value, final JSONNode to) {
		return add(name, DFloat.valueOf(value), to);
	}
	/**
	* {@inheritDoc}
	*/
	public boolean add/*set*/(final String name, final boolean value, final JSONNode to) {
		return add(name, core.Boolean.valueOf(value), to);
	}
	/**
	* {@inheritDoc}
	*/
	public JSONNode addNamedNode(final String name, final byte sequenceStart, final JSONNode to) {
		if (sequenceStart > ZERO) { //case must create an object mode property
			if (to.isJSONObject()) {
				return to.asJSONObject().add_obj_prop(name);
			}
			else if (to.isJSONProperty()) {
				return to.asJSONProperty().add_obj_prop_to_obj_val(name);
			}
		}
		else {
			if (to.isJSONObject()) {
				return to.asJSONObject().add_arr_prop(name, jsonArrayNodeForm, 8/*anticipatedMinNumOfItems*/, forceIntArrayIfPossible, mustConvertToJsOnObjectList == yes/*mustConvertToJsOnObjectList*/);
			}
			else if (to.isJSONProperty()) {
				return to.asJSONProperty().add_arr_prop_to_obj_val(name, jsonArrayNodeForm, 8/*anticipatedMinNumOfItems*/, forceIntArrayIfPossible, mustConvertToJsOnObjectList == yes/*mustConvertToJsOnObjectList*/);
			}
		}
		err(1/*errNum*/, "cannot add a property to a non JSONObject node", 2/*mthdNum*/); return null; //dummy return
	}

	/**
	* {@inheritDoc}
	*/
	@SuppressWarnings({"rawtypes", "unchecked"})
	public boolean addScalar(core.Value value, JSONNode to) {
		if (to.isJSONDocument()) {
			JSONDocument doc = to.asJSONDocument();
			if (doc.element != null) {
				err(1/*errNum*/, "cannot have more than one document top element", 3/*mthdNum*/); return false; //dummy return
			}
			doc.element = value == null ? core.Value.NULL : value;
			return true;
		}
		err(2/*errNum*/, "cannot add standalone scalar value to a non document JSON node", 3/*mthdNum*/); return false; //dummy return
	}
	/**
	* {@inheritDoc}
	*/
	public boolean addScalar(final int value, final JSONNode to) {
		return addScalar(getIntegerNumber(value), to);
	}
	/**
	* {@inheritDoc}
	*/
	public boolean addScalar(final long value, final JSONNode to) {
		return addScalar(__getIntegerNumber(value), to);
	}
	/**
	* {@inheritDoc}
	*/
	public boolean addScalar(final float value, final JSONNode to) {
		return addScalar(SFloat.valueOf(value), to);
	}
	/**
	* {@inheritDoc}
	*/
	public boolean addScalar(final double value, final JSONNode to) {
		return addScalar(DFloat.valueOf(value), to);
	}
	/**
	* {@inheritDoc}
	*/
	public boolean addScalar(final boolean value, final JSONNode to) {
		return addScalar(core.Boolean.valueOf(value), to);
	}

	/**
	* {@inheritDoc}
	*/
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void addItem(core.Thing item, final JSONNode to) {
		if (to.isJSONArrayBuilder()) {
			to.asJSONArrayBuilder().addItem(item);
		}
		else if (to.isJSONList()) {
			JSONList list = to.asJSONList();
			list.add(item);
		}
		else {
			if (to.isJSONProperty()) {
				JSONProperty prop = to.asJSONProperty();
				if (prop.array_value_is_being_acc()) {
					prop.get_json_array_value_builder().addItem(item);
				}
				else if (prop.has_json_list_value()) {
					JSONList list = prop.get_json_list_value();
					list.add(item);
				}
			}
			else {
				err(1/*errNum*/, "cannot add array item to a non JSON array node", 5/*mthdNum*/);
			}
		}
	}
	/**
	* {@inheritDoc}
	*/
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void addItem(final int item, final JSONNode to) {
		if (to.isJSONArrayBuilder()) {
			to.asJSONArrayBuilder().addItem(item);
		}
		else if (to.isJSONList()) {
			JSONList list = to.asJSONList();
			list.add(getIntegerNumber(item));
		}
		else {
			if (to.isJSONProperty()) {
				JSONProperty prop = to.asJSONProperty();
				if (prop.array_value_is_being_acc()) {
					prop.get_json_array_value_builder().addItem(item);
				}
				else if (prop.has_json_list_value()) {
					JSONList list = prop.get_json_list_value();
					list.add(getIntegerNumber(item));
				}
			}
			else {
				err(2/*errNum*/, "cannot add array item to a non JSON array node", 5/*mthdNum*/);
			}
		}
	}
	/**
	* {@inheritDoc}
	*/
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void addItem(final long item, final JSONNode to) {
		if (to.isJSONArrayBuilder()) {
			to.asJSONArrayBuilder().addItem(item);
		}
		else if (to.isJSONList()) {
			JSONList list = to.asJSONList();
			list.add(__getIntegerNumber(item));
		}
		else {
			if (to.isJSONProperty()) {
				JSONProperty prop = to.asJSONProperty();
				if (prop.array_value_is_being_acc()) {
					prop.get_json_array_value_builder().addItem(item);
				}
				else if (prop.has_json_list_value()) {
					JSONList list = prop.get_json_list_value();
					list.add(__getIntegerNumber(item));
				}
			}
			else {
				err(3/*errNum*/, "cannot add array item to a non JSON array node", 5/*mthdNum*/);
			}
		}
	}
	/**
	* {@inheritDoc}
	*/
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void addItem(final float item, final JSONNode to) {
		if (to.isJSONArrayBuilder()) {
			to.asJSONArrayBuilder().addItem(item);
		}
		else if (to.isJSONList()) {
			JSONList list = to.asJSONList();
			list.add(SFloat.valueOf(item));
		}
		else {
			if (to.isJSONProperty()) {
				JSONProperty prop = to.asJSONProperty();
				if (prop.array_value_is_being_acc()) {
					prop.get_json_array_value_builder().addItem(item);
				}
				else if (prop.has_json_list_value()) {
					JSONList list = prop.get_json_list_value();
					list.add(SFloat.valueOf(item));
				}
			}
			else {
				err(4/*errNum*/, "cannot add array item to a non JSON array node", 5/*mthdNum*/);
			}
		}
	}
	/**
	* {@inheritDoc}
	*/
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void addItem(final double item, final JSONNode to) {
		if (to.isJSONArrayBuilder()) {
			to.asJSONArrayBuilder().addItem(item);
		}
		else if (to.isJSONList()) {
			JSONList list = to.asJSONList();
			list.add(DFloat.valueOf(item));
		}
		else {
			if (to.isJSONProperty()) {
				JSONProperty prop = to.asJSONProperty();
				if (prop.array_value_is_being_acc()) {
					prop.get_json_array_value_builder().addItem(item);
				}
				else if (prop.has_json_list_value()) {
					JSONList list = prop.get_json_list_value();
					list.add(DFloat.valueOf(item));
				}
			}
			else {
				err(5/*errNum*/, "cannot add array item to a non JSON array node", 5/*mthdNum*/);
			}
		}
	}
	/**
	* {@inheritDoc}
	*/
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void addItem(final boolean item, final JSONNode to) {
		if (to.isJSONArrayBuilder()) {
			to.asJSONArrayBuilder().addItem(item);
		}
		else if (to.isJSONList()) {
			JSONList list = to.asJSONList();
			list.add(core.Boolean.valueOf(item));
		}
		else {
			if (to.isJSONProperty()) {
				JSONProperty prop = to.asJSONProperty();
				if (prop.array_value_is_being_acc()) {
					prop.get_json_array_value_builder().addItem(item);
				}
				else if (prop.has_json_list_value()) {
					JSONList list = prop.get_json_list_value();
					list.add(core.Boolean.valueOf(item));
				}
			}
			else {
				err(6/*errNum*/, "cannot add array item to a non JSON array node", 5/*mthdNum*/);
			}
		}
	}
	@SuppressWarnings({"unchecked", "rawtypes"})
	final JSONNode newJSONArrayNode() {
		switch(jsonArrayNodeForm)
		{
		case JSONArrayForms.ARRAY:
			return new JSONArrayBuilder(8/*anticipatedMinNumOfItems*/, forceIntArrayIfPossible, false/*arrayListFormPreferred*/, (mustConvertToJsOnObjectList == yes)/*mustConvertToJsOnObjectList*/);
		case JSONArrayForms.LINKED_LIST:
			return new JSONLinkedList();
		case JSONArrayForms.ARRAY_LIST:
			return mustConvertToJsOnObjectList == yes ? new JSONObjectArrayList(8/*anticipatedMinNumOfItems*/) : new JSONArrayList(8/*anticipatedMinNumOfItems*/);
		case (byte)-1:
			return new JSONArrayBuilder(8/*anticipatedMinNumOfItems*/, forceIntArrayIfPossible, true/*arrayListFormPreferred*/, (mustConvertToJsOnObjectList == yes)/*mustConvertToJsOnObjectList*/);
		default:
			return new JSONArrayBuilder(8/*anticipatedMinNumOfItems*/, forceIntArrayIfPossible, true/*arrayListFormPreferred*/, (mustConvertToJsOnObjectList == yes)/*mustConvertToJsOnObjectList*/);
		}
	}
	
	/**
	* {@inheritDoc}
	*/
	@SuppressWarnings({"unchecked", "rawtypes"})
	public JSONNode addObjectStart(final JSONNode to) {
		if (to.isJSONDocument()) {
			JSONDocument doc = to.asJSONDocument();
			if (doc.element != null) {
				err(1/*errNum*/, "cannot have more than one document top element", 10/*mthdNum*/); 
			}
			JSONObject jsonObj = newJSONObject();
			doc.element = jsonObj;
			return jsonObj;
		}
		return addStartItemNode(no/*sequenceStart*/, to);
	}
	/**
	* {@inheritDoc}
	* @param arrayForm meant to tell if the form of the sequence node must be the array form - ignored because there is one form of sequence node in JSON unlike in YAML
	*/
	@SuppressWarnings({"unchecked", "rawtypes"})
	public JSONNode addSequenceStart(final boolean arrayForm, final JSONNode to) {
		if (to.isJSONDocument()) {
			JSONDocument doc = to.asJSONDocument();
			if (doc.element != null) {
				err(1/*errNum*/, "cannot have more than one document top element", 4/*mthdNum*/); 
			}
			JSONNode arrayNode = newJSONArrayNode();
			doc.element = arrayNode;
			return arrayNode;
		}
		return addStartItemNode((arrayForm ? maybe : yes)/*sequenceStart*/, to);
	}
	/**
	* {@inheritDoc}
	*/
	@SuppressWarnings({"unchecked", "rawtypes"})
	public JSONNode addStartItemNode(final byte sequenceStart, final JSONNode to) {
		if (to.isJSONList()) {
			JSONList list = to.asJSONList();
			JSONNode arrayNode = sequenceStart == no ? newJSONObject() : newJSONArrayNode(); //JSONObjectFactory
			list.add(arrayNode);
			return arrayNode;
		}
		else if (to.isJSONArrayBuilder()) {
			JSONNode arrayNode = sequenceStart == no ? newJSONObject() : newJSONArrayNode();
			to.asJSONArrayBuilder().addItem(arrayNode);
			return arrayNode;
		}
		else if (to.isJSONProperty()) {
			JSONProperty prop = to.asJSONProperty();
			if (prop.array_value_is_being_acc()) {
				JSONNode arrayNode = sequenceStart == no ? newJSONObject() : newJSONArrayNode();
				prop.get_json_array_value_builder().addItem(arrayNode);
				return arrayNode;
			}
			else if (prop.has_json_list_value()) {
				JSONNode arrayNode = sequenceStart == no ? newJSONObject() : newJSONArrayNode();
				JSONList list = prop.get_json_list_value();
				list.add(arrayNode);
				return arrayNode;
			}
		}
		err(1/*errNum*/, "cannot start a sequence/array child node within a non JSON array node", 6/*mthdNum*/); return null; //dummy return
	}
	/**
	* {@inheritDoc}
	*/
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void onComplete(final JSONNode node, final JSONNode parent) {
		node.trim();
		if (parent == null) return ;
		if (node.isJSONArrayBuilder()) {
			if (parent.isJSONArrayBuilder()) {
				parent.asJSONArrayBuilder().addItem(node.asJSONArrayBuilder().toArrayNode());
			}
			else if (parent.isJSONList()) {
				JSONList lst = parent.asJSONList();
				lst.add(node.asJSONArrayBuilder().toArrayNode());
			}
			else if (parent.isJSONProperty()) {
				parent.asJSONProperty().set_val(node.asJSONArrayBuilder().toArrayNode());
			}
			else if (parent.isJSONDocument()) {
				JSONDocument doc = parent.asJSONDocument();
				doc.element = node.asJSONArrayBuilder().toArrayNode();
			}
			else {
				//should normally never get here, but...
				err(1/*errNum*/, "don't know how to handle a complete child JSON node (parenClass=" + parent.getClass().getName()  + ", childNodeClass=" + node.getClass().getName() + ")", 9/*mthdNum*/);
			}
		}
		else if (parent.isJSONArrayBuilder()) {
			//parent.asJSONArrayBuilder().addItem(node); //BUG-FIX - 2018-04-26 - was adding the node while it was already added; also was not triming the node either ==> merely trim the node!!!
			node.trim();
			
			//NOTE: no special notification to the parent: there is no need for such...
		}
		//NOTE: default implementation does nothing more, sub-classes may do additional things!!!
	}


}
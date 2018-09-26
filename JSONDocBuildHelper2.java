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
* This is a concrete class for providing support for simple document build helpers for JSON.<br>
* Any instance of this class returns, through finalize method, a {@link JSONDocument JSONDocument} with a content which is a core.Value, a core.Array, a JSONObject or a JSONList.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public class JSONDocBuildHelper2 extends JSONDocBuildHelper<JSONDocument<core.Thing>> {

	
	/**
	* Constructor.<br>
	*/
	public JSONDocBuildHelper2(final byte jsonArrayNodeForm, final boolean forceIntArrayIfPossible, final boolean arrayListFormPreferred, final JSONObjectFactory standaloneJsonObjFactory) {
		super(jsonArrayNodeForm, forceIntArrayIfPossible, arrayListFormPreferred, standaloneJsonObjFactory);
	}
	/**
	* Constructor.<br>
	*/
	public JSONDocBuildHelper2(byte jsonArrayNodeForm, final boolean forceIntArrayIfPossible, final boolean arrayListFormPreferred) {
		super(jsonArrayNodeForm, forceIntArrayIfPossible, arrayListFormPreferred);
	}
	/**
	* Constructor.<br>
	*/
	public JSONDocBuildHelper2(byte jsonArrayNodeForm) {
		super(jsonArrayNodeForm);
	}
	/**
	* Constructor.<br>
	* The form of arrays to build is {@link json.JSONArrayForms#ARRAY ARRAY}.<br>
	*/
	public JSONDocBuildHelper2() {
		super();
	}
	/**
	* {@inheritDoc}
	*/
	public JSONDocBuildHelper2 setMustConvertToJsonObjectList() {
		setMustConvertToJsonObjectList(true); return this; 
	}
	
	/**
	* {@inheritDoc}
	*/
	public final JSONDocument<core.Thing> createFinalizedEmpty() {
		return new JSONDocument<core.Thing>(String.EMPTY);
	}
	/**
	* {@inheritDoc}
	*/
	public final JSONDocument<core.Thing> finalize(final JSONDocument<core.Thing> doc) {
		if (doc.element.isDOMNode()) {
			JSONNode topNode = doc.element.asDOMNode().asJSONNode();
			if (topNode.isJSONArrayBuilder()) {
				doc.element = topNode.asJSONArrayBuilder().toArrayNode();
			}
		}
		return doc;
	}

}
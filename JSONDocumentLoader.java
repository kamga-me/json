package json;

import string.String;
import core.document.IDocumentLoader;
import core.document.ISimpleDocBuildHelper;
import json.stream.IJSONStreamReader;
import core.io.ICharReader;

import static json.stream.IJSONSreamEventTypes.*;
import static core.data.stream.IValueTypes.*;

import core.collection.SmartSimpleSet;
import core.collection.SmartStringSet;
import core.collection.BigSmartStringSet;

import static core.io.IAbsoluteResourceIDTypes.*;
import static core.io.AbsoluteResourceIDTypes.checkIfAbsolueID;

import static core.IOperatingSystemConstants/*SystemProps*/.fileNameComponentSep;

import static core.util.GetInstanceHelper.*;
import core.logging.Logger;
import core.util.SysPropUtil;

import core.io.encoding.IStringDecoder;
import core.io.IByteIterator;
import java.io.InputStream;

import core.io.StreamableInput;
import core.io.CharInputStreamUtil;

import core.document.IDocumentLoadContext; 
import core.document.DocumentLoadConstants;

import core.io.encoding.IStringDecoderProvider;
import static json.stream.JSONStreamReaderFactory.*;
import json.stream.JSONStreamReader;


/**
* Generic class for providing support for JSON document loaders.<br>
* <br><b>NOTE</b>: sub-classes that change the base type of parameter type <code>N</n> must override protected methods {@link #__getEmptyArray() __getEmptyArray()} and {@link #__newArray(int) __newArray(int)}.<br>
*
* @param <N> the base class for nodes
* @param <D> the class for nodes of kind document
* @param <S> the base class for sequences/arrays
* @param <T> the target class of the complete documents
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*
* @see mime.IDocumentLoaderProvider IDocumentLoaderProvider
*/
public class JSONDocumentLoader<N extends core.Thing, D extends N, S extends N, T, DocBuildHelper extends ISimpleDocBuildHelper<N,D,S,T>> extends core.document.DocumentLoader<N,D,T,DocBuildHelper> implements IJSONDocumentLoader<N,D,S,T,DocBuildHelper> { //NOTE: extends IDocumentLoader since 2018-04-23

	/**
	* Helps determine whether to use a string table for property names or not - used if not specified in load-document content.<br>
	* This is just in case string tables are not handled within the JSON stream reader which is the one best suited for handling string: the eralier strings are shared, the better it is.<br>
	*/
	/*protected */boolean propertyNamesAlreadyRefs;
	//protected SmartSimpleSet<String> propNameSet; //NOTE: commented out to make the loader thread safe, it is each load method instance that will create its own property name table
	/**
	* Helps determine whether to use a string table to string property values or not - used if not specified in load-document content.<br>
	* This is just in case string tables are not handled within the JSON stream reader which is the one best suited for handling string: the eralier strings are shared, the better it is.<br>
	*/
	/*protected */boolean useStringTblForPropValues;
	//protected SmartSimpleSet<String> stringValueSet; //NOTE: commented out to make the loader thread safe, it is each load method instance that will create its own property value table

	/**
	* TODO - effectively take this into account - 
	*/
//	protected boolean preferReadFromMemoryMappedFiles; //NOTE - 2018-04-24 - NOW HANDLE THROUGH LOAD CONTEXT!!!
	
	/**
	* Constructor.<br>
	* @param propertyNamesAlreadyRefs helps determine whether to use a string table for property names or not - used if not specified in load-document content.
	* @param useStringTblForPropValues helps determine whether to use a string table to string property values or not - used if not specified in load-document content.
	*/
	public JSONDocumentLoader(final boolean propertyNamesAlreadyRefs, final boolean useStringTblForPropValues/*, final boolean fastFileRead*//*preferReadFromMemoryMappedFiles*/) {
		super();
		this.propertyNamesAlreadyRefs = propertyNamesAlreadyRefs;
		//this.propNameSet = propertyNamesAlreadyRefs ? null : new SmartStringSet<String>(10);
		this.useStringTblForPropValues = useStringTblForPropValues;
		//this.stringValueSet = !useStringTblForPropValues ? null : new SmartStringSet<String>(16);
		//this.preferReadFromMemoryMappedFiles = fastFileRead;
	}
//	/**
//	* Constructor.<br>
//	*/
//	protected JSONDocumentLoader(final boolean propertyNamesAlreadyRefs, final boolean useStringTblForPropValues) {
//		this(propertyNamesAlreadyRefs, useStringTblForPropValues, false/*fastFileRead*/);
//	}
	/**
	* Constructor.<br>
	* @param propertyNamesAlreadyRefs helps determine whether to use a string table for property names or not - used if not specified in load-document content.
	*/
	public JSONDocumentLoader(final boolean propertyNamesAlreadyRefs) {
		this(propertyNamesAlreadyRefs, false/*useStringTblForPropValues*/);
	}
	/**
	* Constructor.<br>
	*/
	public JSONDocumentLoader() {
		this(false/*propertyNamesAlreadyRefs*/);
	}
	
	protected final void load_err(final int errNum, final CharSequence errMsg) {
		err(errNum, errMsg, "load");
	}
	
	protected String getPropName(IJSONStreamReader<? extends String> jsonStreamReader) {
		//NOTE! must used getValueType and not getRawValueType, as the former returns two values for string: STRING and URI ==> don't bother trying to handle bdb type CSTRING, for example
		switch(jsonStreamReader.getValueType())
		{
		case STRING:
		case URI:
			return jsonStreamReader.getString();
		}
		return jsonStreamReader.getLiteralAsString();
	}
	
	final String get_ref_prop_nm(final String name, final byte namesFromBufConvAssumedRefs, final boolean forceUseOfStringTbl, final SmartSimpleSet<String> propNameSet) {
		if (namesFromBufConvAssumedRefs == yes || (namesFromBufConvAssumedRefs == maybe && propertyNamesAlreadyRefs) && !forceUseOfStringTbl) return name;
		String nm = propNameSet.addIfNotPresent(name);
		return nm == null ? name : nm;
	}
	final String get_ref_prop_str_val(final String value, final byte valuesFromBufConvAssumedRefs, final SmartSimpleSet<String> stringValueSet) {
		if (valuesFromBufConvAssumedRefs == yes || (valuesFromBufConvAssumedRefs == maybe && !useStringTblForPropValues)) return value;
		String v = stringValueSet.addIfNotPresent(value);
		return v == null ? value : v;
	}
	/**
	* {@inheritDoc}
	*/
	public T load(IJSONStreamReader<? extends String> jsonStreamReader, final DocBuildHelper docBuildHelper, byte namesFromBufConvAssumedRefs, byte valuesFromBufConvAssumedRefs) {
		return load_(jsonStreamReader, docBuildHelper, namesFromBufConvAssumedRefs < (byte)0 ? no :namesFromBufConvAssumedRefs == (byte)0 ? maybe : yes , valuesFromBufConvAssumedRefs < (byte)0 ? no : valuesFromBufConvAssumedRefs == (byte)0 ? maybe : no, String.EMPTY/*systemId*/);
	}
	/**
	* {@inheritDoc}
	*/
	public T load(IJSONStreamReader<? extends String> jsonStreamReader, final DocBuildHelper docBuildHelper) {
		return load(jsonStreamReader, docBuildHelper, maybe/*namesFromBufConvAssumedRefs*/, maybe/*valuesFromBufConvAssumedRefs*/);
	}
	
	/**
	* {@inheritDoc}
	*/
	public T load(final String systemId, final DocBuildHelper docBuildHelper, final IDocumentLoadContext loadContext) {
		byte namesFromBufConvAssumedRefs = loadContext != null ? loadContext.namesFromStringBufferConvAssumedRefs() : maybe;
		byte valuesFromBufConvAssumedRefs = loadContext != null ? loadContext.valuesFromStringBufferConvAssumedRefs() : maybe;
		JSONStreamReader<? extends String, ?> jsonStreamReader = newJSONSreamReader(systemId, loadContext);
		T doc = load_(jsonStreamReader, docBuildHelper, namesFromBufConvAssumedRefs, valuesFromBufConvAssumedRefs, systemId);
		if (jsonStreamReader.next() != END_OF_STREAM) {
			load_err(4, "EOF expected");
		}
		return doc;
	}
	/**
	* {@inheritDoc}
	*/
	public final T load(final String systemId, final DocBuildHelper docBuildHelper) {
		return load(systemId, docBuildHelper, null);
	}
	/**
	* {@inheritDoc}
	*/
	public T load(final java.lang.String systemId, final DocBuildHelper docBuildHelper, final IDocumentLoadContext loadContext) {
		byte namesFromBufConvAssumedRefs = loadContext != null ? loadContext.namesFromStringBufferConvAssumedRefs() : maybe;
		byte valuesFromBufConvAssumedRefs = loadContext != null ? loadContext.valuesFromStringBufferConvAssumedRefs() : maybe;
		JSONStreamReader<? extends String, ?> jsonStreamReader = newJSONSreamReader(systemId, loadContext);
		T doc = load_(jsonStreamReader, docBuildHelper, namesFromBufConvAssumedRefs, valuesFromBufConvAssumedRefs, systemId);
		if (jsonStreamReader.next() != END_OF_STREAM) {
			load_err(5, "EOF expected");
		}
		return doc;
	}
	/**
	* {@inheritDoc}
	*/
	public final T load(final java.lang.String systemId, final DocBuildHelper docBuildHelper) {
		return load(systemId, docBuildHelper, null);
	}
	/**
	* {@inheritDoc}
	*/
	public T load(final ICharReader sourceReader, final DocBuildHelper docBuildHelper, final IDocumentLoadContext loadContext) {
		byte namesFromBufConvAssumedRefs = loadContext != null ? loadContext.namesFromStringBufferConvAssumedRefs() : maybe;
		byte valuesFromBufConvAssumedRefs = loadContext != null ? loadContext.valuesFromStringBufferConvAssumedRefs() : maybe;
		JSONStreamReader<? extends String, ?> jsonStreamReader = newJSONSreamReader(sourceReader, loadContext);
		return load_(jsonStreamReader, docBuildHelper, namesFromBufConvAssumedRefs, valuesFromBufConvAssumedRefs, String.EMPTY/*systemId*/);
	}
	/**
	* {@inheritDoc}
	*/
	public final T load(final ICharReader sourceReader, final DocBuildHelper docBuildHelper) {
		return load(sourceReader, docBuildHelper, null);
	}
	/**
	* {@inheritDoc}
	*/
	public T load(final StreamableInput input, final DocBuildHelper docBuildHelper, final IDocumentLoadContext loadContext) {
		byte namesFromBufConvAssumedRefs = loadContext != null ? loadContext.namesFromStringBufferConvAssumedRefs() : maybe;
		byte valuesFromBufConvAssumedRefs = loadContext != null ? loadContext.valuesFromStringBufferConvAssumedRefs() : maybe;
		JSONStreamReader<? extends String, ?> jsonStreamReader = newJSONSreamReader(input, loadContext);
		return load_(jsonStreamReader, docBuildHelper, namesFromBufConvAssumedRefs, valuesFromBufConvAssumedRefs, String.EMPTY/*systemId*/);
	}
	/**
	* {@inheritDoc}
	*/
	public final T load(final StreamableInput input, final DocBuildHelper docBuildHelper) {
		return load(input, docBuildHelper, null);
	}
	
	private final void load_err(final int errNum, final CharSequence msg, final IJSONStreamReader<? extends String> jsonStreamReader, final Exception cause, final CharSequence systemId) {
		//NOTE: method new_xcptn already concatenate the message of the cause and the error message ==> DON'T DO IT HERE AGAIN!!! ==> COMMENTED OUT
		throw new_xcptn(/*(cause == null ? "" : cause.getMessage() + core.IOperatingSystemConstants.LINE_TERMINATOR) + */"JSONDocumentLoader::load_-" + errNum + ": load error - " + msg + " (offset=" + jsonStreamReader.getOffset() + ", lineNumber=" + jsonStreamReader.getLineNumber() + ", lineStartOffset=" + jsonStreamReader.getLineStartOffset() + ", systemId=" + systemId + ")", cause);
	}
	
	private final T load_(IJSONStreamReader<? extends String> jsonStreamReader, final DocBuildHelper docBuildHelper, byte namesFromBufConvAssumedRefs, byte valuesFromBufConvAssumedRefs, final CharSequence systemId) {
		try 
		{
			return __load(jsonStreamReader, docBuildHelper, namesFromBufConvAssumedRefs < (byte)0 ? no :namesFromBufConvAssumedRefs == (byte)0 ? maybe : yes , valuesFromBufConvAssumedRefs < (byte)0 ? no : valuesFromBufConvAssumedRefs == (byte)0 ? maybe : no);
		}
		catch(json.stream.JSONStreamException ex)
		{
			load_err(1/*errNum*/, "JSON stream error", jsonStreamReader, ex/*cause*/, systemId);
		}
		catch(json.JSONLoadException ex)
		{
			load_err(2/*errNum*/, ex.getMessage(), jsonStreamReader, ex/*cause*/, systemId);
		}
		catch(json.JSONIOException ex)
		{
			load_err(3/*errNum*/, "I/O error", jsonStreamReader, ex/*cause*/, systemId);
		}
		catch(json.JSONException ex)
		{
			load_err(4/*errNum*/, "JSON error", jsonStreamReader, ex/*cause*/, systemId);
		}
		catch(Exception ex)
		{
			load_err(5/*errNum*/, "Runtime error", jsonStreamReader, ex/*cause*/, systemId);
		}
		return null; //dummy return
	}
	
	private final T __load(IJSONStreamReader<? extends String> jsonStreamReader, final DocBuildHelper docBuildHelper, byte namesFromBufConvAssumedRefs, byte valuesFromBufConvAssumedRefs) {
		N currentNode = null;
		D document = null;
		byte eventType = jsonStreamReader.next();
		switch(eventType)
		{
		case OBJECT_START: 
		case ARRAY_START: 
			document = docBuildHelper.startDoc();
			break ;
		case SCALAR: 
			document = docBuildHelper.startDoc();
			byte valueType = jsonStreamReader.getValueType();
			switch(valueType)
			{
			case INT:
			case TINYINT:
			case SHORTINT:
				docBuildHelper.addScalar(jsonStreamReader.getInt(), document); break ;
			case LONGINT:
				docBuildHelper.addScalar(jsonStreamReader.getLong(), document); break ;
			case DFLOAT:
				docBuildHelper.addScalar(jsonStreamReader.getDouble(), document); break ;
			case SFLOAT:
				docBuildHelper.addScalar(jsonStreamReader.getFloat(), document); break ;
			case BOOLEAN:
				docBuildHelper.addScalar(jsonStreamReader.getBoolean(), document); break ;
			case STRING:
			case URI:
				docBuildHelper.addScalar(jsonStreamReader.getString(), document); break ;
			default:
				if (valueType > LAST_EXT_VALUE_TYPE_NUM) {
					docBuildHelper.addScalar(jsonStreamReader.getValueArray().ToString(), document); break ; //QUESTION: which delimiter best suits?!!
				}
				docBuildHelper.addScalar(jsonStreamReader.getValue(), document); break ;
			}
			return docBuildHelper.finalize(document);
		case EMPTY_JSON_STREAM: 
			return docBuildHelper.createFinalizedEmpty();
		default:
			load_err(1/*errNum*/, "bad JSON stream");
		}
		if (eventType == OBJECT_START) {
			currentNode = docBuildHelper.addObjectStart(document);
		}
		else {
			currentNode = docBuildHelper.addSequenceStart(/*arrayForm*/true, document);
		}
		NodeStack stack = null; //new NodeStack();
		String name = null;
		byte valueType = 0;
		boolean isNotStringKey = false;
		SmartStringSet<String> propNameSet = propertyNamesAlreadyRefs ? null : new SmartStringSet<String>(10);
		SmartStringSet<String> stringValueSet = !useStringTblForPropValues ? null : new SmartStringSet<String>(16);
		N child;
		___loop:
		do 
		{
			eventType = jsonStreamReader.next();
			switch(eventType)
			{
			case PROPERTY_NAME: 
				//NOTE! must used getValueType and not getRawValueType, as the former returns two values for string: STRING and URI ==> don't bother trying to handle bdb type CSTRING, for example
				valueType = jsonStreamReader.getValueType(); //will sere to know if the key is not a string key
				isNotStringKey = valueType != STRING && valueType != URI;
				name = get_ref_prop_nm(getPropName(jsonStreamReader), namesFromBufConvAssumedRefs, isNotStringKey, propNameSet);
				if ((eventType = jsonStreamReader.next()) != PROPERTY_VALUE) {
					switch(eventType)
					{
					case PROPERTY_OBJECT_START:
						//NOTE! must used getValueType and not getRawValueType, as the former returns two values for string: STRING and URI ==> don't bother trying to handle bdb type CSTRING, for example
						valueType = jsonStreamReader.getValueType(); //will sere to know if the key is not a string key
						isNotStringKey = valueType != STRING && valueType != URI;
						name = get_ref_prop_nm(getPropName(jsonStreamReader), namesFromBufConvAssumedRefs, isNotStringKey, propNameSet);
						child = docBuildHelper.addNamedNode(name, no/*sequenceStart*/, currentNode);
						if (stack == null) {
							stack = new NodeStack(5);
						}
						stack.push(currentNode);
						currentNode = child;
						continue ___loop;
					case PROPERTY_ARRAY_START: 
						//NOTE! must used getValueType and not getRawValueType, as the former returns two values for string: STRING and URI ==> don't bother trying to handle bdb type CSTRING, for example
						valueType = jsonStreamReader.getValueType(); //will sere to know if the key is not a string key
						isNotStringKey = valueType != STRING && valueType != URI;
						name = get_ref_prop_nm(getPropName(jsonStreamReader), namesFromBufConvAssumedRefs, isNotStringKey, propNameSet);
						child = docBuildHelper.addNamedNode(name, maybe/*sequenceStart*/, currentNode);
						if (stack == null) {
							stack = new NodeStack(5);
						}
						stack.push(currentNode);
						currentNode = child;
						continue ___loop;
					}
					//should never get here , but...
					load_err(2, "bad JSON stream - property value expected (eventType=" + eventType + ")");
				}
				valueType = jsonStreamReader.getValueType();
				switch(valueType)
				{
				case INT:
				case TINYINT:
				case SHORTINT:
					docBuildHelper.add(name, jsonStreamReader.getInt(), currentNode); continue ___loop;
				case LONGINT:
					docBuildHelper.add(name, jsonStreamReader.getLong(), currentNode); continue ___loop;
				case DFLOAT:
					docBuildHelper.add(name, jsonStreamReader.getDouble(), currentNode); continue ___loop;
				case SFLOAT:
					docBuildHelper.add(name, jsonStreamReader.getFloat(), currentNode); continue ___loop;
				case BOOLEAN:
					docBuildHelper.add(name, jsonStreamReader.getBoolean(), currentNode); continue ___loop;
				case STRING:
				case URI:
					docBuildHelper.add(name, get_ref_prop_str_val(jsonStreamReader.getString(), valuesFromBufConvAssumedRefs, stringValueSet), currentNode); continue ___loop;
				default:
					if (valueType > LAST_EXT_VALUE_TYPE_NUM) {
						docBuildHelper.add(name, jsonStreamReader.getValueArray(), currentNode); continue ___loop;
					}
					docBuildHelper.add(name, jsonStreamReader.getValue(), currentNode); continue ___loop;
				}
			case ARRAY_SIMPLE_ITEM: 
			case PROPERTY_ARRAY_SIMPLE_ITEM: 
				valueType = jsonStreamReader.getValueType();
				switch(valueType)
				{
				case INT:
				case TINYINT:
				case SHORTINT:
					docBuildHelper.addItem(jsonStreamReader.getInt(), currentNode); continue ___loop;
				case LONGINT:
					docBuildHelper.addItem(jsonStreamReader.getLong(), currentNode); continue ___loop;
				case DFLOAT:
					docBuildHelper.addItem(jsonStreamReader.getDouble(), currentNode); continue ___loop;
				case SFLOAT:
					docBuildHelper.addItem(jsonStreamReader.getFloat(), currentNode); continue ___loop;
				case BOOLEAN:
					docBuildHelper.addItem(jsonStreamReader.getBoolean(), currentNode); continue ___loop;
				case STRING:
				case URI:
					docBuildHelper.addItem(get_ref_prop_str_val(jsonStreamReader.getString(), valuesFromBufConvAssumedRefs, stringValueSet), currentNode); continue ___loop;
				default:
					if (valueType > LAST_EXT_VALUE_TYPE_NUM) {
						docBuildHelper.addItem(jsonStreamReader.getValueArray(), currentNode); continue ___loop;
					}
					docBuildHelper.addItem(jsonStreamReader.getValue(), currentNode); continue ___loop;
				}
			case OBJECT_START:
			case PROPERTY_ARRAY_OBJ_ITM_START: 
				child = docBuildHelper.addStartItemNode(no/*sequenceStart*/, currentNode);
				if (stack == null) {
					stack = new NodeStack(5);
				}
				stack.push(currentNode);
				currentNode = child;
				continue ___loop;
			case ARRAY_START: 
			case PROPERTY_ARRAY_ARR_ITM_START: 
				child = docBuildHelper.addStartItemNode(maybe/*sequenceStart*/, currentNode);
				if (stack == null) {
					stack = new NodeStack(5);
				}
				stack.push(currentNode);
				currentNode = child;
				continue ___loop;
			case OBJECT_END:
			case ARRAY_END:
			case PROPERTY_OBJECT_END:
			case PROPERTY_ARRAY_END: 
			case PROPERTY_ARRAY_OBJ_ITM_END: 
			case PROPERTY_ARRAY_ARR_ITM_END: 
				//NOTE: the stream reader guarantees that an end event that fires corresponds the last start event that fired ==> don't bother trying to make a distinction, just close current...
				if (stack != null && !stack.isEmpty()) {
					N parent = stack.pop();
					docBuildHelper.onComplete(currentNode, parent);
					currentNode = parent;
					continue ___loop;
				}
				else {
//					System.out.println("JSONDocumentLoader - About to call onComplete");
					docBuildHelper.onComplete(currentNode, document);
//					System.out.println("JSONDocumentLoader - About to finalize");
					return docBuildHelper.finalize(document);
				}
			default:
				//should normally never get here, but...
				load_err(3, "unknown or not yet effectively handled JSON stream event (" + eventType + ")");
			}
		} while (true); //end ___loop
	}
	
	/*Difficult to implement for something that is meant just to double check in case someone uses a bad IJSNStreamReader class which violate the contrat set by the interface by not guaranteeing that end event corresponds to the last start event
	* Because it is complex and difficult to implement ==> COMMENTED OUt ANd DROPPED!!!
	final void checkEndEvent(final byte eventType, final N currentNode) {
		switch(eventType)
		{
		case OBJECT_END:
		case PROPERTY_ARRAY_OBJ_ITM_END: 
		case ARRAY_END:
		case PROPERTY_ARRAY_ARR_ITM_END: 
		case PROPERTY_OBJECT_END:
		case PROPERTY_ARRAY_END: 
		}
	}*/

	@Override
	protected final JSONLoadException new_xcptn(/*final */java.lang.String errMsg, final java.lang.Throwable cause){ //NOTE: added argument cause on 2018-04-24
		if (cause != null) {
			errMsg = cause.getMessage() + core.IOperatingSystemConstants.LINE_TERMINATOR + errMsg;
		}
		return new JSONLoadException(errMsg, cause);
	}

}
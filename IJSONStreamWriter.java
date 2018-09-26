package json.stream;

import string.ICharSequence;
import string.String;

/**
* Interface that JSON stream writers implement.<br>
*
* @author Marc E. KAMGA
* @version 1.0
* @copyright Marc E. KAMGA
*/
public interface IJSONStreamWriter {

	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeObjectDocStart();
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayDocStart();
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc/*writeScalarDoc*/(ICharSequence scalar);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc/*writeScalarDoc*/(String scalar);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc/*writeScalarDoc*/(String scalar, boolean cstring);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc/*writeScalarDoc*/(int scalar);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc/*writeScalarDoc*/(long scalar);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc/*writeScalarDoc*/(byte scalar);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc/*writeScalarDoc*/(short scalar);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc/*writeScalarDoc*/(float scalar);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc/*writeScalarDoc*/(double scalar);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc/*writeScalarDoc*/(boolean scalar);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc/*writeScalarDoc*/(core.binary.Binary scalar);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc/*writeScalarDoc*/(core.Value scalar);
	
	
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc(String[] scalar); //since 2018-03-09
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc(String[] scalar, boolean cstring); //since 2018-03-09
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc(int[] scalar); //since 2018-03-09
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc(long[] scalar); //since 2018-03-09
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc(byte[] scalar); //since 2018-03-09
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc(short[] scalar); //since 2018-03-09
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc(float[] scalar); //since 2018-03-09
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc(double[] scalar); //since 2018-03-09
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc(boolean[] scalar); //since 2018-03-09
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeSimpleDoc(core.Value[] scalar); //since 2018-03-09
	
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, int value);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, long value);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, byte value);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, short value);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, float value);
	/**
	* Writes a {@link core.io.bdb.IBDBValueTypes#NUMBER8 NUMBER8} property.br>
	* @throws RuntimeException - the proper stream exception, depending on the JSON form - if the supplied double-precision floating point value is not a valid {@link core.io.bdb.IBDBValueTypes#NUMBER8 NUMBER8} value.
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeN8Prop(String name, float value); //since 2018-03-21
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, double value);
	/**
	* Writes a {@link core.io.bdb.IBDBValueTypes#NUMBER17 NUMBER17} property.br>
	* @throws RuntimeException the proper exception (e.g. BDBException or BDBStreamException if the stream is of category BDB), depending on the JSON form - if the supplied double-precision floating point value is not a valid {@link core.io.bdb.IBDBValueTypes#NUMBER17 NUMBER17} value.
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeN17Prop(String name, double value); //since 2018-03-21
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, boolean value);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, ICharSequence value);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, String value);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, String value, boolean cstring);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, core.binary.Binary value);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, core.Value value);
	/**
	* @return <code>this.writeNullProp(name,{@link core.IValueTypes.#NULL_VALUE NULL_VALUE})</code>
	*/
	public IJSONStreamWriter/*void*/ writeNullProp(String name);
	/**
	* Serves to write properties with typed null values.<br>
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeNullProp(String name, final byte valueType); //since 2018-03-07 //
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, int[] value, int off, int len);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, long[] value, int off, int len);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, byte[] value, int off, int len);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, short[] value, int off, int len);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, float[] value, int off, int len);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, double[] value, int off, int len);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, boolean[] value, int off, int len);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, String[] value, int off, int len, boolean cstrings);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeProp(String name, core.Value[] value, int off, int len);
	
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeObjectPropStart(String name);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayPropStart(String name);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeObjectArrayItemStart();
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayArrayItemStart();
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(int value);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(long value);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(byte value);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(short value);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(float value);
	/**
	* Writes a {@link core.io.bdb.IBDBValueTypes#NUMBER8 NUMBER8} array item.br>
	* @throws RuntimeException the proper exception (e.g. BDBException or BDBStreamException if the stream is of category BDB), depending on the JSON form - if the supplied single-precision floating point value is not a valid {@link core.io.bdb.IBDBValueTypes#NUMBER8 NUMBER8} value.
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeN8ArrayItem(float value); //since 2018-03-21
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(double value);
	/**
	* Writes a {@link core.io.bdb.IBDBValueTypes#NUMBER17 NUMBER17} array item.br>
	* @throws RuntimeException the proper exception (e.g. BDBException or BDBStreamException if the stream is of category BDB), depending on the JSON form - if the supplied double-precision floating point value is not a valid {@link core.io.bdb.IBDBValueTypes#NUMBER17 NUMBER17} value.
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeN17ArrayItem(double value); //since 2018-03-21
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(boolean value);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(ICharSequence value);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(String value);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(String value, boolean cstring);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(core.binary.Binary value);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(core.Value value);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeNullArrayItem();
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(int[] value, int off, int len);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(long[] value, int off, int len);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(byte[] value, int off, int len);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(short[] value, int off, int len);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(float[] value, int off, int len);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(double[] value, int off, int len);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(boolean[] value, int off, int len);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(String[] value, int off, int len, boolean cstrings);
	/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeArrayItem(core.Value[] value, int off, int len);
	
	/**
	* Ends the current array, object or document.<br>/**
	* @return <code>this</code>
	*/
	public IJSONStreamWriter/*void*/ writeCurrentEnd();
	
	/**
	* Declares a field given the name and the value type.<br>
	* @param name the name of the field
	* @param valueType the alue type of the field
	* @return <code>0x80000000</code> if the format is not JSONB and declaring fields bring no added value to the JSON writer; the number allocated to the declared field, otherwise.
	* @throws SecurityException if fields can no longer be declared
	*
	* @see core.IValueTypes IValueTypes
	* @see core.data.type.IValueDataTypes IValueDataTypes
	* @see core.io.bdb.IBDBValueTypes IValueTypes
	*/
	public int declare(String name, byte valueType, byte sharedStringTypeOrArrayItemsKind); //since 2018-03-09
	/**
	* @return <code>declare(name,valueType,&lt;default&gt;)</code>
	*/
	public int declare(String name, byte valueType); //since 2018-03-09
	/**
	* Declares a string field associated with an enum.<br>
	* @param name the name of the field to declare
	* @param enumValues the array containing the enumerated string values
	* @throws IllegalArgumentException if <code>enumValues</code> is equal to null
	* @return <code>0x80000000</code> if the format is not JSONB and declaring fields bring no added value to the JSON writer; <code>-(&lt;number&gt; + 1)</code> if a field with the same name exist but is not a string field; the number allocated to the declared field, otherwise.
	*/
	public int declare(String name, boolean cstringField, String[] enumValues, int enumValsOff, int enumValsCount, final boolean unbounded); //since 2018-03-09
	/**
	* @return <code>declare(name,cstringField,enumValues,0,enumValues.length,unbounded)</code>
	*/
	public int declare(String name, boolean cstringField, String[] enumValues, final boolean unbounded); //since 2018-03-09
	
	/**
	* Flushes the internal buffer.<br>
	* @return <code>false</code> if cannot or not allowed to flush the writer; 
	*/
	public boolean/*void*/ flush(); //NOTE: changed returned type on 2018-03-09
	
	/**
	* Flushes the still buffered data and then closes the stream and releases the allocated resources.<br>
	*/
	public void close();
	
}